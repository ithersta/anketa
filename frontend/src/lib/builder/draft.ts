import type { SurveyEntry } from "$lib/survey/entries";

export type SurveyDraft = {
    id?: number,
    title: string,
}

export type SurveyDraftEntry = {
    id?: number,
    surveyId: number,
    order: number,
    content: SurveyEntry,
}
