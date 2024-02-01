import Dexie, { type Table } from "dexie";
import type { SurveyDraft, SurveyDraftEntry } from "$lib/builder/draft";

export class AnketaDexie extends Dexie {
    surveyDrafts!: Table<SurveyDraft>
    surveyDraftEntries!: Table<SurveyDraftEntry>

    constructor() {
        super("anketa");
        this.version(1).stores({
            surveyDrafts: "++id",
            surveyDraftEntries: "++id, surveyId",
        })
    }
}

export const db = new AnketaDexie()