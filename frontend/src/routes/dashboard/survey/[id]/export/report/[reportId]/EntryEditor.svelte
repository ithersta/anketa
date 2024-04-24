<script lang="ts">
    import TextEntryEditor from "./TextEntryEditor.svelte";
    import TextFieldEntryEditor from "./TextFieldEntryEditor.svelte";
    import MultiChoiceEntryEditor from "./MultiChoiceEntryEditor.svelte";
    import PolarChoiceEntryEditor from "./PolarChoiceEntryEditor.svelte";
    import type { ReportEntryUiState } from "$lib/report/entries";
    import type { SurveyContent } from "$lib/survey/survey";
    import { get } from "svelte/store";

    export let uiState: ReportEntryUiState
    export let forceError: boolean
    export let survey: SurveyContent
    let surveyEntry = survey.entries.find(e => e.id === get(uiState.entry).forEntryWithId)
</script>

{#if uiState.type === "Text"}
    <TextEntryEditor {uiState} {forceError}/>
{:else if uiState.type === "TextField"}
    <TextFieldEntryEditor {uiState} {forceError} {surveyEntry}/>
{:else if uiState.type === "MultiChoice"}
    <MultiChoiceEntryEditor {uiState} {forceError} {surveyEntry}/>
{:else if uiState.type === "PolarChoice"}
    <PolarChoiceEntryEditor {uiState} {forceError} {surveyEntry}/>
{:else}
    <span class="text-error">Error: Unknown entry type</span>
{/if}
