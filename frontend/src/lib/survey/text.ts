import { derived, readable, type Readable, writable, type Writable } from "svelte/store";
import type { ValidationHint } from "$lib/survey/validation";
import { NilUUID } from "$lib/builder/uuid";

export namespace Text {
    export type Entry = {
        type: "Text",
        id: string,
        content: string,
    }

    export type UiState = {
        entry: Entry,
        answer: Readable<undefined>,
        hints: Readable<ValidationHint[]>,
        clear: () => void,
    }

    export function toUiState(entry: Entry): UiState {
        return {
            entry: entry,
            answer: readable(undefined),
            hints: readable([]),
            clear: () => {},
        }
    }

    export namespace Builder {
        export type Hint = {
            type: "CannotBeEmpty",
        } & ValidationHint

        export type UiState = {
            type: "Text",
            content: Writable<string>,
            entry: Readable<Entry>,
            hints: Readable<Hint[]>,
        }

        export function toUiState(initial?: Entry): UiState {
            const content = writable(initial?.content ?? "")
            const entry = derived(
                content,
                ($content) => {
                    return {
                        type: "Text",
                        id: NilUUID,
                        content: $content,
                    } satisfies Entry
                },
            )
            const hints = derived(entry, ($entry) => validate($entry))
            return {
                type: "Text",
                content: content,
                entry: entry,
                hints: hints,
            }
        }

        function validate(entry: Entry): Hint[] {
            return [
                {
                    type: "CannotBeEmpty",
                    isError: entry.content.length === 0,
                    isHint: false,
                },
            ]
        }
    }
}