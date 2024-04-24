import type { ValidationHint } from "$lib/survey/validation";
import { derived, type Readable, writable, type Writable } from "svelte/store";
import { MultiChoice } from "$lib/survey/multichoice";

export namespace MultiChoiceReport {
    export type Entry = {
        type: "MultiChoice",
        forEntryWithId: string,
        template: string,
        doSummarise: boolean,
    }

    export type Hint = ValidationHint

    export type UiState = {
        type: "MultiChoice",
        template: Writable<string>,
        doSummarise: Writable<boolean>
        entry: Readable<Entry>,
        hints: Readable<Hint[]>,
    }

    export function fromSurveyEntry(entry: MultiChoice.Entry): Entry {
        return {
            type: "MultiChoice",
            forEntryWithId: entry.id,
            template: "",
            doSummarise: false,
        }
    }

    export function toUiState(initial: Entry): UiState {
        const template = writable(initial.template)
        const doSummarise = writable(initial.doSummarise)
        const entry = derived(
            [template, doSummarise],
            ([$template, $doSummarise]) => {
                return {
                    type: "MultiChoice",
                    forEntryWithId: initial.forEntryWithId,
                    template: $template,
                    doSummarise: $doSummarise,
                } satisfies Entry
            },
        )
        const hints = derived(entry, () => [])
        return {
            type: "MultiChoice",
            template: template,
            doSummarise: doSummarise,
            entry: entry,
            hints: hints,
        }
    }
}
