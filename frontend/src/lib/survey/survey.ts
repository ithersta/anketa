import { isValid, type SurveyAnswer, type SurveyEntry } from "$lib/survey/entries";

export type SurveyContent = {
    title: string,
    entries: SurveyEntry[],
}

export function areAnswersValid(content: SurveyContent, answers: Map<string, SurveyAnswer>): boolean {
    return content.entries.every((entry) => isValid(entry, answers.get(entry.id)))
}