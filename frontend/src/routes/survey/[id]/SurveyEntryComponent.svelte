<script lang="ts">
    import type { SurveyEntryUiState } from "$lib/survey/entries";
    import Text from "./Text.svelte";
    import TextField from "./TextField.svelte";
    import MultiChoice from "./MultiChoice.svelte";
    import PolarChoice from "./PolarChoice.svelte";

    export let uiState: SurveyEntryUiState
    export let forceError: boolean
</script>

{#if (uiState.entry.type === "TextField")}
    <TextField {uiState} {forceError}/>
{:else if (uiState.entry.type === "MultiChoice")}
    <MultiChoice {uiState} {forceError}/>
{:else if (uiState.entry.type === "PolarChoice")}
    <PolarChoice {uiState} {forceError}/>
{:else if (uiState.entry.type === "Text")}
    <Text {uiState}/>
{:else}
    <span class="text-error">Error: Unknown entry type</span>
{/if}
