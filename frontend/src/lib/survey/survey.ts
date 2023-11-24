import type { SurveyEntry } from "$lib/survey/entries";

export type SurveyContent = {
    title: string,
    entries: SurveyEntry[],
}