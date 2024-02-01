<script lang="ts">
    import type { SurveyEntryBuilderUiState } from "$lib/survey/entries";
    import TextEntryEditor from "./TextEntryEditor.svelte";
    import TextFieldEntryEditor from "./TextFieldEntryEditor.svelte";
    import MultiChoiceEntryEditor from "./MultiChoiceEntryEditor.svelte";
    import PolarChoiceEntryEditor from "./PolarChoiceEntryEditor.svelte";

    export let uiState: SurveyEntryBuilderUiState
    export let forceError: boolean
</script>

{#if uiState.type === "Text"}
    <TextEntryEditor {uiState} {forceError}/>
{:else if uiState.type === "TextField"}
    <TextFieldEntryEditor {uiState} {forceError}/>
{:else if uiState.type === "MultiChoice"}
    <MultiChoiceEntryEditor {uiState} {forceError}/>
{:else if uiState.type === "PolarChoice"}
    <PolarChoiceEntryEditor {uiState} {forceError}/>
{:else}
    <span class="text-error">Error: Unknown entry type</span>
{/if}