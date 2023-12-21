import type { ValidationHint } from "$lib/survey/validation";
import { requiredHint } from "$lib/survey/validation";
import { derived, type Readable, type Writable } from "svelte/store";
import { persisted } from "svelte-persisted-store";
import * as devalue from "devalue";

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

    export type UiState = {
        entry: Entry,
        selected: Writable<Set<number>>,
        other: Writable<string>,
        answer: Readable<Answer | undefined>,
        hints: Readable<Hint[]>,
        clear: () => void,
    }

    export function toUiState(entry: Entry, prefix: string): UiState {
        const selected: Writable<Set<number>> = persisted(`${prefix}${entry.id}-selected`, new Set(), { serializer: devalue })
        const other: Writable<string> = persisted(`${prefix}${entry.id}-other`, "")
        const nullifiedOther: Readable<string | null> = derived(
            other,
            ($other) => ($other === "") ? null : $other
        )
        const answer: Readable<Answer | undefined> = derived(
            [selected, nullifiedOther],
            ([$selected, $nullifiedOther]) => {
                if ($selected.size === 0 && $nullifiedOther === null) return undefined
                return {
                    type: "MultiChoice",
                    selected: Array.from($selected),
                    other: $nullifiedOther
                } satisfies MultiChoice.Answer
            }
        )
        const hints: Readable<Hint[]> = derived(
            answer,
            ($answer) => validate(entry, $answer)
        )
        const isRadio = MultiChoice.isRadio(entry)
        nullifiedOther.subscribe(($nullifiedOther) => {
            if ($nullifiedOther !== null && isRadio) {
                selected.update((s) => { s.clear(); return s })
            }
        })
        return {
            entry: entry,
            selected: selected,
            other: other,
            answer: answer,
            hints: hints,
            clear: () => {
                selected.update((s) => { s.clear(); return s })
                other.set("")
            },
        }
    }

    export function isRadio(entry: Entry): boolean {
        return entry.maxSelected === 1 && entry.minSelected === 1
    }

    function validate(entry: Entry, answer: Answer | undefined): Hint[] {
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