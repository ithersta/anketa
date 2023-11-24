import type { ValidationHint } from "$lib/survey/validation";

export namespace PolarChoice {
    export type Entry = {
        type: "PolarChoice",
        id: string,
        isRequired: boolean,
        question: string,
        range: number,
    }

    export type Answer = number | undefined

    export type Hint = {
        type: "Required"
    } & ValidationHint

    export function validate(entry: Entry, answer: Answer): Hint[] {
        return [
            {
                type: "Required",
                isHint: false,
                isError: answer === undefined && entry.isRequired,
            }
        ]
    }
}