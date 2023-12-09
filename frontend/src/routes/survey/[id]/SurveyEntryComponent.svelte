<script lang="ts">
    import type { SurveyAnswer, SurveyEntry } from "$lib/survey/entries";
    import Text from "./Text.svelte";
    import TextField from "./TextField.svelte";
    import MultiChoice from "./MultiChoice.svelte";
    import PolarChoice from "./PolarChoice.svelte";

    export let entry: SurveyEntry
    export let forceError: boolean
    export let post: (uuid: string, answer: SurveyAnswer) => void
</script>

{#if (entry.type === "TextField")}
    <TextField {entry} {forceError} {post}/>
{:else if (entry.type === "MultiChoice")}
    <MultiChoice {entry} {forceError} {post}/>
{:else if (entry.type === "PolarChoice")}
    <PolarChoice {entry} {forceError} {post}/>
{:else if (entry.type === "Text")}
    <Text {entry}/>
{:else}
    <span class="text-error">Error: Unknown entry kind</span>
{/if}