import type { ValidationHint } from "$lib/survey/validation";

export namespace TextField {
    export type Entry = {
        type: "TextField",
        id: string,
        isRequired: boolean,
        question: string,
        minLength: number,
        maxLength: number,
    }

    export type Answer = string | undefined

    export type Hint = {
        type: "Required" | "MaxLengthExceeded" | "MinLengthNotMatched"
    } & ValidationHint

    export function validate(entry: Entry, answer: Answer): Hint[] {
        let isFilled = answer !== undefined || entry.isRequired
        let length = answer?.length ?? 0
        return [
            {
                type: "Required",
                isHint: false,
                isError: answer === undefined && entry.isRequired,
            },
            {
                type: "MaxLengthExceeded",
                isHint: length >= entry.minLength,
                isError: length > entry.maxLength
            },
            {
                type: "MinLengthNotMatched",
                isHint: length < entry.minLength,
                isError: isFilled && length < entry.minLength,
            }
        ]
    }
}