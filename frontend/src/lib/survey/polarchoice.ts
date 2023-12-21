import type { ValidationHint } from "$lib/survey/validation";
import { requiredHint } from "$lib/survey/validation";
import { derived, type Readable, type Writable } from "svelte/store";
import { persisted } from "svelte-persisted-store";
import * as devalue from "devalue";

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
        type: "Required"
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
                    selected: Number.parseInt($selected)
                } satisfies PolarChoice.Answer
            }
        )
        const hints = derived(
            answer,
            ($answer) => validate(entry, $answer)
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
}