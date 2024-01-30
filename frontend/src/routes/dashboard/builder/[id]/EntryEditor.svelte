<script lang="ts">
    import type { SurveyEntryBuilderUiState } from "$lib/survey/entries";
    import TextEntryEditor from "./TextEntryEditor.svelte";

    export let uiState: SurveyEntryBuilderUiState
    export let forceError: boolean
</script>

{#if uiState.type === "Text"}
    <TextEntryEditor {uiState} {forceError}/>
{:else}
    <span class="text-error">Error: Unknown entry type</span>
{/if}