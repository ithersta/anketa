import { MultiChoice } from "$lib/survey/multichoice";
import { PolarChoice } from "$lib/survey/polarchoice";
import { TextField } from "$lib/survey/textfield";
import { Text } from "$lib/survey/text";

export type SurveyEntry = MultiChoice.Entry |
    PolarChoice.Entry |
    TextField.Entry |
    Text.Entry

export type SurveyEntryUiState = MultiChoice.UiState |
    PolarChoice.UiState |
    TextField.UiState |
    Text.UiState

export type SurveyAnswer = MultiChoice.Answer |
    PolarChoice.Answer |
    TextField.Answer

export type SurveyEntryBuilderUiState = MultiChoice.Builder.UiState |
    PolarChoice.Builder.UiState |
    TextField.Builder.UiState |
    Text.Builder.UiState

export function toUiState(entry: SurveyEntry, prefix: string): SurveyEntryUiState {
    if (entry.type === "MultiChoice") {
        return MultiChoice.toUiState(entry, prefix)
    } else if (entry.type === "PolarChoice") {
        return PolarChoice.toUiState(entry, prefix)
    } else if (entry.type === "TextField") {
        return TextField.toUiState(entry, prefix)
    } else if (entry.type === "Text") {
        return Text.toUiState(entry)
    }
    throw new Error("Unknown entry type")
}

export function toBuilderUiState(entry: SurveyEntry): SurveyEntryBuilderUiState {
    if (entry.type === "MultiChoice") {
        return MultiChoice.Builder.toUiState(entry)
    } else if (entry.type === "PolarChoice") {
        return PolarChoice.Builder.toUiState(entry)
    } else if (entry.type === "TextField") {
        return TextField.Builder.toUiState(entry)
    } else if (entry.type === "Text") {
        return Text.Builder.toUiState(entry)
    }
    throw new Error("Unknown entry type")
}