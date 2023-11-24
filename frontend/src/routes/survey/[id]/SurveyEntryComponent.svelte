<script lang="ts">
    import type { SurveyAnswer, SurveyEntry } from "$lib/survey/entries";
    import Text from "./Text.svelte";
    import TextField from "./TextField.svelte";
    import MultiChoice from "./MultiChoice.svelte";
    import PolarChoice from "./PolarChoice.svelte";

    export let entry: SurveyEntry
    export let answer: SurveyAnswer
    export let forceError: boolean
</script>

{#if (entry.type === "TextField")}
    <TextField {entry} {forceError} bind:answer={answer}/>
{:else if (entry.type === "MultiChoice")}
    <MultiChoice {entry} {forceError} bind:answer={answer}/>
{:else if (entry.type === "PolarChoice")}
    <PolarChoice {entry} {forceError} bind:answer={answer}/>
{:else if (entry.type === "Text")}
    <Text {entry}/>
{:else}
    <span class="text-error">Error: Unknown entry kind</span>
{/if}