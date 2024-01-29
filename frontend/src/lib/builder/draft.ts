import type { SurveyEntry } from "$lib/survey/entries";

export type SurveyDraft = {
    id: number,
    title: string,
    entries: SurveyDraftEntry[],
}

export type SurveyDraftEntry = {
    id: number,
    order: number,
    content: SurveyEntry,
}