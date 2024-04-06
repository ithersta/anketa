import { derived, type Readable, writable, type Writable } from "svelte/store";
import type { ValidationHint } from "$lib/survey/validation";

export namespace TextReport {
    export type Entry = {
        type: "Text",
        content: string,
    }

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
