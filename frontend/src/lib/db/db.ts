import Dexie, { type Table } from "dexie";
import type { SurveyDraft, SurveyDraftEntry } from "$lib/builder/draft";
import type { ReportDraft, ReportDraftEntry, Summarization } from "$lib/report/draft";

export class AnketaDexie extends Dexie {
    surveyDrafts!: Table<SurveyDraft>
    surveyDraftEntries!: Table<SurveyDraftEntry>
    reportDrafts!: Table<ReportDraft>
    reportDraftEntries!: Table<ReportDraftEntry>
    summarizations!: Table<Summarization>

    constructor() {
        super("anketa");
        this.version(3).stores({
            surveyDrafts: "++id",
            surveyDraftEntries: "++id, surveyId",
            reportDrafts: "++id, surveyId",
            reportDraftEntries: "++id, draftId",
            summarizations: "++entryId",
        })
    }
}

export const db = new AnketaDexie()
