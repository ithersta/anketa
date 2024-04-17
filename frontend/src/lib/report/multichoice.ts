import type { ValidationHint } from "$lib/survey/validation";
import { derived, type Readable, writable, type Writable } from "svelte/store";
import { MultiChoice } from "$lib/survey/multichoice";

export namespace MultiChoiceReport {
    export type Entry = {
        type: "MultiChoice",
        forEntryWithId: string,
        template: string,
    }

    export type Hint = ValidationHint

    export type UiState = {
        type: "MultiChoice",
        template: Writable<string>,
        entry: Readable<Entry>,
        hints: Readable<Hint[]>,
    }

    export function fromSurveyEntry(entry: MultiChoice.Entry): Entry {
        return {
            type: "MultiChoice",
            forEntryWithId: entry.id,
            template: "",
        }
    }

    export function toUiState(initial: Entry): UiState {
        const template = writable(initial.template)
        const entry = derived(
            template,
            ($template) => {
                return {
                    type: "MultiChoice",
                    forEntryWithId: initial.forEntryWithId,
                    template: $template,
                } satisfies Entry
            },
        )
        const hints = derived(entry, () => [])
        return {
            type: "MultiChoice",
            template: template,
            entry: entry,
            hints: hints,
        }
    }
}
