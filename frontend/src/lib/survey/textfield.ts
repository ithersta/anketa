import type { ValidationHint } from "$lib/survey/validation";
import { requiredHint } from "$lib/survey/validation";

export namespace TextField {
    export type Entry = {
        type: "TextField",
        id: string,
        isRequired: boolean,
        question: string,
        minLength: number,
        maxLength: number,
    }

    export type Answer = {
        type: "TextField",
        text: string,
    }

    export type Hint = {
        type: "MaxLengthExceeded" | "MinLengthNotMatched" | "Required"
    } & ValidationHint

    export function validate(entry: Entry, answer: Answer | undefined): Hint[] {
        if (answer === undefined && !entry.isRequired) return []
        let length = answer?.text?.length ?? 0
        return [
            requiredHint(entry, answer),
            {
                type: "MaxLengthExceeded",
                isHint: length >= entry.minLength,
                isError: length > entry.maxLength
            },
            {
                type: "MinLengthNotMatched",
                isHint: length < entry.minLength,
                isError: answer !== undefined && length < entry.minLength,
            },
        ]
    }
}