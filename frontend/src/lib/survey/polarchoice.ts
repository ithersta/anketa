import type { ValidationHint } from "$lib/survey/validation";
import { requiredHint } from "$lib/survey/validation";
import { derived, type Readable, writable, type Writable } from "svelte/store";
import { persisted } from "svelte-persisted-store";
import * as devalue from "devalue";
import { NilUUID } from "$lib/builder/uuid";

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
    }

    export type Hint = {
        type: "Required",
    } & ValidationHint

    export type UiState = {
        entry: Entry,
        selected: Writable<string | undefined>,
        answer: Readable<Answer | undefined>,
        hints: Readable<Hint[]>,
        clear: () => void,
    }

    export function toUiState(entry: Entry, prefix: string): UiState {
        const selected: Writable<string | undefined> = persisted(`${prefix}${entry.id}`, undefined, { serializer: devalue })
        const answer: Readable<Answer | undefined> = derived(
            selected,
            ($selected) => {
                if ($selected === undefined) return undefined
                return {
                    type: "PolarChoice",
                    selected: Number.parseInt($selected),
                } satisfies PolarChoice.Answer
            },
        )
        const hints = derived(
            answer,
            ($answer) => validate(entry, $answer),
        )
        return {
            entry: entry,
            selected: selected,
            answer: answer,
            hints: hints,
            clear: () => { selected.set(undefined) },
        }
    }

    function validate(entry: Entry, answer: Answer | undefined): Hint[] {
        return [requiredHint(entry, answer)]
    }

    export namespace Builder {
        export type Hint = {
            type: string, // TODO
        } & ValidationHint

        export type UiState = {
            type: "PolarChoice",
            isRequired: Writable<boolean>,
            question: Writable<string>,
            range: Writable<number>,
            entry: Readable<Entry>,
            hints: Readable<Hint[]>,
        }

        export function toUiState(initial?: Entry): UiState {
            const isRequired = writable(initial?.isRequired ?? true)
            const question = writable(initial?.question ?? "")
            const range = writable(initial?.range ?? 2)
            const entry = derived(
                [isRequired, question, range],
                ([$isRequired, $question, $range]) => {
                    return {
                        type: "PolarChoice",
                        id: NilUUID,
                        isRequired: $isRequired,
                        question: $question,
                        range: $range,
                    } satisfies Entry
                },
            )
            const hints = derived(entry, ($entry) => validate($entry))
            return {
                type: "PolarChoice",
                isRequired: isRequired,
                question: question,
                range: range,
                entry: entry,
                hints: hints,
            }
        }

        function validate(entry: Entry): Hint[] {
            // TODO
        }
    }
}