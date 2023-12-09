import { MultiChoice } from "$lib/survey/multichoice";
import { PolarChoice } from "$lib/survey/polarchoice";
import { TextField } from "$lib/survey/textfield";
import type { Text } from "$lib/survey/text";
import type { ValidationHint } from "$lib/survey/validation";

export type SurveyEntry = MultiChoice.Entry |
    PolarChoice.Entry |
    TextField.Entry |
    Text.Entry

export type SurveyAnswer = MultiChoice.Answer |
    PolarChoice.Answer |
    TextField.Answer

export function isValid(entry: SurveyEntry, answer: SurveyAnswer): boolean {
    let hints: ValidationHint[]
    if (entry.type === "MultiChoice" && (answer === undefined || answer.type === "MultiChoice")) {
        hints = MultiChoice.validate(entry, answer)
    } else if (entry.type === "PolarChoice" && (answer === undefined || answer.type === "PolarChoice")) {
        hints = PolarChoice.validate(entry, answer)
    } else if (entry.type === "TextField" && (answer === undefined || answer.type === "TextField")) {
        hints = TextField.validate(entry, answer)
    } else if (entry.type === "Text") {
        hints = []
    } else {
        return false
    }
    return hints.every((hint) => !hint.isError)
}