<script lang="ts">
    import { Button } from "$lib/components/ui/button";
    import type { SurveyContent } from "$lib/survey/survey";
    import type { SurveyAnswer } from "$lib/survey/entries";
    import SurveyEntryComponent from "./SurveyEntryComponent.svelte";

    export let data: SurveyContent

    let forceError = false
    let answers: Record<number, SurveyAnswer> = {}
</script>

<div class="max-w-prose mx-auto p-4">
    <div class="prose">
        <h1 class="pt-16 decoration-primary-500 decoration-4">{data.title}</h1>
    </div>
    {#each data.entries as entry (entry.id)}
        <div class="py-2">
            <SurveyEntryComponent {entry} {forceError} bind:answer={answers[entry.id]}/>
        </div>
    {/each}
    <div class="flex justify-end py-2">
        <Button on:click={() => forceError = true}>Отправить</Button>
    </div>
</div>