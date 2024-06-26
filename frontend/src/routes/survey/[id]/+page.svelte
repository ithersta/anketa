<script lang="ts">
    import { Button } from "$lib/components/ui/button";
    import { type SurveyContent } from "$lib/survey/survey";
    import SurveyEntryComponent from "./SurveyEntryComponent.svelte";
    import { type SurveyAnswer, toUiState } from "$lib/survey/entries";
    import { signSurveyAnswers } from "$lib/crypto/sign";
    import { derived, get, type Readable } from "svelte/store";
    import { safeFetch } from "$lib/safeFetch";
    import { goto } from "$app/navigation";

    export let data: {
        id: string,
        survey: SurveyContent,
    }

    const localStoragePrefix = `answer-${data.id}-`
    const uiStates = data.survey.entries.map((e) => toUiState(e, localStoragePrefix))
    const answersValid: Readable<boolean> = derived(
        uiStates.map((uiState) => uiState.hints),
        (allHints) => allHints.every((hints) => hints.every((hint) => hint.isError === false))
    )
    let forceError = false
    let requestCount = 0

    async function submit() {
        if (requestCount > 0) return
        forceError = true
        if (!$answersValid) {
            return
        }
        requestCount += 1
        const answers: Map<string, SurveyAnswer> = new Map()
        uiStates.forEach((uiState) => {
            const answer: SurveyAnswer | undefined = get(uiState.answer)
            if (answer !== undefined) {
                answers.set(uiState.entry.id, answer)
            }
        })
        let signedMessage = await signSurveyAnswers(JSON.stringify(Object.fromEntries(answers)))
        let response = await safeFetch(`/survey/${data.id}`, {
            method: "POST",
            body: JSON.stringify(signedMessage),
        })
        if (response && response.ok) {
            await goto("/survey/success")
        }
        requestCount -= 1
    }
</script>

<svelte:head>
    <title>{data.survey.title}</title>
</svelte:head>

<div class="max-w-prose mx-auto p-4">
    <div class="flex pt-16 pb-4">
        <div class="flex-grow">
            <h1 class="text-4xl font-bold">{data.survey.title}</h1>
        </div>
    </div>
    {#each uiStates as uiState (uiState.entry.id)}
        <div class="py-2">
            <SurveyEntryComponent {uiState} {forceError}/>
        </div>
    {/each}
    <div class="flex justify-end py-2 space-x-4 items-center">
        {#if (!$answersValid && forceError)}
            <span class="text-destructive">Исправьте ошибки</span>
        {/if}
        <Button variant={$answersValid ? "default" : "secondary"} on:click={submit} disabled={requestCount > 0}>Отправить</Button>
    </div>
</div>
