import type { ValidationHint } from "$lib/survey/validation";
import { requiredHint } from "$lib/survey/validation";

export namespace PolarChoice {
    export type Entry = {
        type: "PolarChoice",
        id: string,
        isRequired: boolean,
        question: string,
        range: number,
    }

    export type Answer = {
        type: "PolarChoice",
        selected: number,
    } | undefined

    export type Hint = {
        type: "Required"
    } & ValidationHint

    export function validate(entry: Entry, answer: Answer): Hint[] {
        return [requiredHint(entry, answer)]
    }
}