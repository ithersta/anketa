<script lang="ts">
    import { Button } from "$lib/components/ui/button";
    import { Sun, Moon } from "lucide-svelte";
    import { areAnswersValid, type SurveyContent } from "$lib/survey/survey";
    import SurveyEntryComponent from "./SurveyEntryComponent.svelte";
    import { toggleMode } from "mode-watcher";
    import type { SurveyAnswer } from "$lib/survey/entries";
    import { signSurveyAnswers } from "$lib/crypto/sign";
    import SuccessDialog from "./SuccessDialog.svelte";

    export let data: {
        id: string,
        survey: SurveyContent,
    }

    let openSuccessDialog = false
    let forceError = false
    let answers: Map<string, SurveyAnswer> = new Map<string, SurveyAnswer>()
    let post = (uuid: string, answer: SurveyAnswer) => {
        if (answer === undefined) {
            answers.delete(uuid)
        } else {
            answers.set(uuid, answer)
        }
        answersValid = areAnswersValid(data.survey, answers)
    }
    let answersValid = false
    async function submit() {
        forceError = true
        if (answersValid) {
            let signedMessage = await signSurveyAnswers(JSON.stringify(Object.fromEntries(answers)))
            let response = await fetch(`/survey/${data.id}`, {
                method: "POST",
                body: JSON.stringify(signedMessage),
            })
            if (response.ok) {
                openSuccessDialog = true
            }
        }
    }
</script>

<SuccessDialog bind:dialogOpen={openSuccessDialog}/>
<div class="max-w-prose mx-auto p-4">
    <div class="flex pt-16">
        <div class="flex-grow">
            <h1 class="text-4xl font-bold">{data.survey.title}</h1>
        </div>

        <Button on:click={toggleMode} variant="outline" size="icon">
            <Sun class="h-[1.2rem] w-[1.2rem] rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0"/>
            <Moon class="absolute h-[1.2rem] w-[1.2rem] rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100"/>
            <span class="sr-only">Toggle theme</span>
        </Button>
    </div>
    {#each data.survey.entries as entry (entry.id)}
        <div class="py-2">
            <SurveyEntryComponent {entry} {forceError} {post}/>
        </div>
    {/each}
    <div class="flex justify-end py-2 space-x-4 items-center">
        {#if (!answersValid && forceError)}
            <span class="text-destructive">Исправьте ошибки</span>
        {/if}
        <Button variant={answersValid ? "default" : "secondary"} on:click={submit}>Отправить</Button>
    </div>
</div>