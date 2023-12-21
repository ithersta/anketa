import type { ValidationHint } from "$lib/survey/validation";
import { requiredHint } from "$lib/survey/validation";

export namespace MultiChoice {
    export type Entry = {
        type: "MultiChoice",
        id: string,
        isRequired: boolean,
        question: string,
        options: string[],
        isAcceptingOther: boolean,
        minSelected: number,
        maxSelected: number,
        otherMaxLength: number | undefined,
    }

    export type Answer = {
        type: "MultiChoice",
        selected: number[],
        other: string | null,
    }

    export type Hint = {
        type: "MinChoiceNotMatched" | "MaxChoiceNotMatched" | "OtherMaxLengthExceeded" | "Required",
    } & ValidationHint

    export function isRadio(entry: Entry): boolean {
        return entry.maxSelected === 1 && entry.minSelected === 1
    }

    export function validate(entry: Entry, answer: Answer | undefined): Hint[] {
        if (answer === undefined && !entry.isRequired) return []
        let selectedCount: number
        if (answer) {
            selectedCount = answer.selected.length + ((answer.other !== null) ? 1 : 0)
        } else {
            selectedCount = 0
        }
        return [
            requiredHint(entry, answer),
            {
                type: "MinChoiceNotMatched",
                isHint: entry.minSelected > 1,
                isError: answer !== undefined && selectedCount < entry.minSelected,
            },
            {
                type: "MaxChoiceNotMatched",
                isHint: entry.maxSelected < entry.options.length && !isRadio(entry),
                isError: selectedCount > entry.maxSelected,
            },
            {
                type: "OtherMaxLengthExceeded",
                isHint: false,
                isError: answer !== undefined &&
                    answer.other !== null &&
                    entry.otherMaxLength !== undefined &&
                    answer.other.length > entry.otherMaxLength,
            }
        ]
    }
}