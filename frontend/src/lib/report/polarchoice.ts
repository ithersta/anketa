import type { ValidationHint } from "$lib/survey/validation";
import { derived, type Readable, writable, type Writable } from "svelte/store";
import { PolarChoice } from "$lib/survey/polarchoice";
import { MultiChoiceReport } from "$lib/report/multichoice";

export namespace PolarChoiceReport {
    export type Entry = {
        type: "PolarChoice",
        forEntryWithId: string,
        template: string,
        chartType: MultiChoiceReport.ChartType,
    }

    export type Hint = ValidationHint

    export type UiState = {
        type: "PolarChoice",
        template: Writable<string>,
        chartType: Writable<MultiChoiceReport.ChartType>,
        entry: Readable<Entry>,
        hints: Readable<Hint[]>,
    }

    export function fromSurveyEntry(entry: PolarChoice.Entry): Entry {
        return {
            type: "PolarChoice",
            forEntryWithId: entry.id,
            template: "",
            chartType: undefined,
        }
    }

    export function toUiState(initial: Entry): UiState {
        const template = writable(initial.template)
        const chartType = writable(initial.chartType)
        const entry = derived(
            [template, chartType],
            ([$template, $chartType]) => {
                return {
                    type: "PolarChoice",
                    forEntryWithId: initial.forEntryWithId,
                    template: $template,
                    chartType: $chartType,
                } satisfies Entry
            },
        )
        const hints = derived(entry, () => [])
        return {
            type: "PolarChoice",
            template: template,
            chartType: chartType,
            entry: entry,
            hints: hints,
        }
    }
}
