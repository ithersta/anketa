import type { ValidationHint } from "$lib/survey/validation";
import { requiredHint } from "$lib/survey/validation";
import { derived, readable, type Readable, writable, type Writable } from "svelte/store";
import { persisted } from "svelte-persisted-store";
import * as devalue from "devalue";
import { NilUUID } from "$lib/uuid";
import { parseIntStrict } from "$lib/parseIntStrict";

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

    export function toPreviewUiState(entry: Entry): UiState {
        return {
            entry: entry,
            selected: writable(new Set()),
            other: writable(""),
            answer: readable(undefined),
            hints: readable(validate(entry, undefined)),
            clear: () => {},
        }
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
                isHint: entry.maxSelected < (entry.options.length + (entry.isAcceptingOther ? 1 : 0)) && !isRadio(entry),
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

    export namespace Builder {
        export type Hint = {
            type: "OptionsEmpty" | "InvalidOptionsRange" | "EmptyQuestion" |
                "InvalidMinSelected" | "InvalidMaxSelected" | "InvalidOtherMaxLength",
        } & ValidationHint

        export type UiState = {
            type: "MultiChoice",
            isRequired: Writable<boolean>,
            question: Writable<string>,
            options: Writable<{ id: number, text: string }[]>,
            isAcceptingOther: Writable<boolean>,
            minSelected: Writable<string>,
            maxSelected: Writable<string>,
            otherMaxLength: Writable<string>,
            entry: Readable<Entry>,
            hints: Readable<Hint[]>,
        }

        export function toUiState(initial?: Entry, isRadio: boolean = false): UiState {
            const isRequired = writable(initial?.isRequired ?? true)
            const question = writable(initial?.question ?? "")
            const options = writable(
                initial?.options?.map((o, index) => ({ id: index, text: o })) ?? []
            )
            const isAcceptingOther = writable(initial?.isAcceptingOther ?? false)
            const minSelected = writable(initial?.minSelected?.toString() ?? "1")
            let initialMaxSelected: string
            if (initial) {
                if (initial.maxSelected === initial.options.length) {
                    initialMaxSelected = ""
                } else {
                    initialMaxSelected = initial.maxSelected.toString()
                }
            } else {
                initialMaxSelected = isRadio ? "1" : ""
            }
            const maxSelected = writable(initialMaxSelected)
            const otherMaxLength = writable(initial?.otherMaxLength?.toString() ?? "100")
            const entry = derived(
                [isRequired, question, options, isAcceptingOther, minSelected, maxSelected, otherMaxLength],
                ([$isRequired, $question, $options, $isAcceptingOther, $minSelected, $maxSelected, $otherMaxLength]) => {
                    return {
                        type: "MultiChoice",
                        id: NilUUID,
                        isRequired: $isRequired,
                        question: $question,
                        options: $options.map(o => o.text),
                        isAcceptingOther: $isAcceptingOther,
                        minSelected: parseIntStrict($minSelected),
                        maxSelected: $maxSelected.length === 0 ? $options.length : parseIntStrict($maxSelected),
                        otherMaxLength: $isAcceptingOther ? parseIntStrict($otherMaxLength) : undefined,
                    } satisfies Entry
                },
            )
            const hints = derived(entry, ($entry) => validate($entry))
            return {
                type: "MultiChoice",
                isRequired: isRequired,
                question: question,
                options: options,
                isAcceptingOther: isAcceptingOther,
                minSelected: minSelected,
                maxSelected: maxSelected,
                otherMaxLength: otherMaxLength,
                entry: entry,
                hints: hints,
            }
        }

        function validate(entry: Entry): Hint[] {
            return [
                {
                    type: "OptionsEmpty",
                    isError: entry.options.length === 0,
                    isHint: false,
                },
                {
                    type: "InvalidMinSelected",
                    isError: isNaN(entry.minSelected),
                    isHint: false,
                },
                {
                    type: "InvalidMaxSelected",
                    isError: isNaN(entry.maxSelected),
                    isHint: false,
                },
                {
                    type: "InvalidOtherMaxLength",
                    isError: entry.otherMaxLength !== undefined && isNaN(entry.otherMaxLength),
                    isHint: false,
                },
                {
                    type: "InvalidOptionsRange",
                    isError: entry.options.length !== 0 &&
                        (entry.minSelected < 0 || entry.minSelected > entry.maxSelected ||
                        entry.maxSelected < 1 || entry.maxSelected > entry.options.length),
                    isHint: false,
                },
                {
                    type: "EmptyQuestion",
                    isError: entry.question.length === 0,
                    isHint: false,
                },
            ]
        }
    }
}