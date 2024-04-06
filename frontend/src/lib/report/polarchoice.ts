import type { ValidationHint } from "$lib/survey/validation";
import { derived, type Readable, writable, type Writable } from "svelte/store";

export namespace PolarChoiceReport {
    export type Entry = {
        type: "PolarChoice",
        forEntryWithId: string,
        template: string,
    }

    export type Hint = ValidationHint

    export type UiState = {
        type: "PolarChoice",
        template: Writable<string>,
        entry: Readable<Entry>,
        hints: Readable<Hint[]>,
    }

    export function toUiState(initial: Entry): UiState {
        const template = writable(initial.template)
        const entry = derived(
            template,
            ($template) => {
                return {
                    type: "PolarChoice",
                    forEntryWithId: initial.forEntryWithId,
                    template: $template,
                } satisfies Entry
            },
        )
        const hints = derived(entry, () => [])
        return {
            type: "PolarChoice",
            template: template,
            entry: entry,
            hints: hints,
        }
    }
}
