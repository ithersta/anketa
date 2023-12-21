import { readable, type Readable } from "svelte/store";
import type { ValidationHint } from "$lib/survey/validation";

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
}