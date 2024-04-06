import type { ValidationHint } from "$lib/survey/validation";
import { derived, type Readable } from "svelte/store";

export namespace TextFieldReport {
    export type Entry = {
        type: "TextField",
        forEntryWithId: string,
    }

    export type Hint = ValidationHint

    export type UiState = {
        type: "TextField",
        entry: Readable<Entry>,
        hints: Readable<Hint[]>,
    }

    export function toUiState(initial: Entry): UiState {
        const entry = derived(
            [],
            () => {
                return {
                    type: "TextField",
                    forEntryWithId: initial.forEntryWithId,
                } satisfies Entry
            },
        )
        const hints = derived([], () => [])
        return {
            type: "TextField",
            entry: entry,
            hints: hints,
        }
    }
}
