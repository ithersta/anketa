import type { ValidationHint } from "$lib/survey/validation";
import { derived, type Readable, writable, type Writable } from "svelte/store";
import { TextField } from "$lib/survey/textfield";

export namespace TextFieldReport {
    export type Entry = {
        type: "TextField",
        forEntryWithId: string,
        doSummarise: boolean,
    }

    export type Hint = ValidationHint

    export type UiState = {
        type: "TextField",
        doSummarise: Writable<boolean>,
        entry: Readable<Entry>,
        hints: Readable<Hint[]>,
    }

    export function fromSurveyEntry(entry: TextField.Entry): Entry {
        return {
            type: "TextField",
            doSummarise: false,
            forEntryWithId: entry.id,
        }
    }

    export function toUiState(initial: Entry): UiState {
        const doSummarise = writable(initial.doSummarise)
        const entry = derived(
            doSummarise,
            ($doSummarise) => {
                return {
                    type: "TextField",
                    doSummarise: $doSummarise,
                    forEntryWithId: initial.forEntryWithId,
                } satisfies Entry
            },
        )
        const hints = derived([], () => [])
        return {
            type: "TextField",
            doSummarise: doSummarise,
            entry: entry,
            hints: hints,
        }
    }
}
