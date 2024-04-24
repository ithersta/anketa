import type { ReportEntryUiState } from "$lib/report/entries";

export type EntryEditorDialogState = {
    id?: number,
    uiState: ReportEntryUiState,
} | undefined
