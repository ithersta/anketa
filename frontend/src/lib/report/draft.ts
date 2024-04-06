import type { ReportEntry } from "$lib/report/entries";

export type ReportDraft = {
    id?: number,
    surveyId: number,
    title: string,
}

export type ReportDraftEntry = {
    id?: number,
    draftId: number,
    order: number,
    content: ReportEntry,
}
