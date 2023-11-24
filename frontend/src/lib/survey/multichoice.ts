import type { ValidationHint } from "$lib/survey/validation";

export namespace MultiChoice {
    export type Entry = {
        type: "MultiChoice",
        id: string,
        isRequired: boolean,
        question: string,
        options: string[],
        isAcceptingOthers: boolean,
        minSelected: number,
        maxSelected: number,
    }

    export type Answer = {
        selected: number[],
        other: string | undefined,
    } | undefined

    export type Hint = {
        type: "MinChoiceNotMatched" | "MaxChoiceNotMatched" | "Required",
    } & ValidationHint

    export function isRadio(entry: Entry): boolean {
        return entry.maxSelected === 1 && entry.minSelected === 1
    }

    export function validate(entry: Entry, answer: Answer): Hint[] {
        let selectedCount: number
        if (answer) {
            selectedCount = answer.selected.length + ((answer.other) ? 1 : 0)
        } else {
            selectedCount = 0
        }
        let isFilled = answer !== undefined || entry.isRequired
        return [
            {
                type: "Required",
                isHint: false,
                isError: answer === undefined && entry.isRequired,
            },
            {
                type: "MinChoiceNotMatched",
                isHint: entry.minSelected > 1,
                isError: isFilled && selectedCount < entry.minSelected,
            },
            {
                type: "MaxChoiceNotMatched",
                isHint: entry.maxSelected < entry.options.length && !isRadio(entry),
                isError: isFilled && selectedCount > entry.maxSelected,
            }
        ]
    }
}