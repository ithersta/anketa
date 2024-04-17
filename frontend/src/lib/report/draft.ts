import type { ReportEntry } from "$lib/report/entries";

export type ReportDraft = {
    id?: number,
    surveyId: string,
    title: string,
}

export type ReportDraftEntry = {
    id?: number,
    draftId: number,
    order: number,
    content: ReportEntry,
}

export type ReportContent = {
    entries: ReportEntry[],
    divideBy: string | undefined,
}
