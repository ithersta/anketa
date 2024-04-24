import { TextReport } from "$lib/report/text";
import { TextFieldReport } from "$lib/report/textfield";
import { PolarChoiceReport } from "$lib/report/polarchoice";
import { MultiChoiceReport } from "$lib/report/multichoice";
import type { SurveyEntry } from "$lib/survey/entries";

export type ReportEntry = TextReport.Entry |
    TextFieldReport.Entry |
    PolarChoiceReport.Entry |
    MultiChoiceReport.Entry

export type ReportEntryUiState = TextReport.UiState |
    TextFieldReport.UiState |
    PolarChoiceReport.UiState |
    MultiChoiceReport.UiState

export function toUiState(entry: ReportEntry): ReportEntryUiState {
    if (entry.type === "MultiChoice") {
        return MultiChoiceReport.toUiState(entry)
    } else if (entry.type === "PolarChoice") {
        return PolarChoiceReport.toUiState(entry)
    } else if (entry.type === "TextField") {
        return TextFieldReport.toUiState(entry)
    } else if (entry.type === "Text") {
        return TextReport.toUiState(entry)
    }
    throw new Error("Unknown entry type")
}

export function fromSurveyEntry(entry: SurveyEntry): ReportEntry | undefined {
    if (entry.type === "MultiChoice") {
        return MultiChoiceReport.fromSurveyEntry(entry)
    } else if (entry.type === "PolarChoice") {
        return PolarChoiceReport.fromSurveyEntry(entry)
    } else if (entry.type === "TextField") {
        return TextFieldReport.fromSurveyEntry(entry)
    } else if (entry.type === "Text") {
        return undefined
    }
    throw new Error("Unknown entry type")
}
