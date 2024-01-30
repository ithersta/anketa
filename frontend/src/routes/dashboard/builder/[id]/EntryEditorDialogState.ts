import type { SurveyEntryBuilderUiState } from "$lib/survey/entries";

export type EntryEditorDialogState = {
    id?: number,
    uiState: SurveyEntryBuilderUiState,
} | undefined