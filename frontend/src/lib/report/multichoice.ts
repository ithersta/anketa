import type { ValidationHint } from "$lib/survey/validation";
import { derived, type Readable, writable, type Writable } from "svelte/store";
import { MultiChoice } from "$lib/survey/multichoice";

export namespace MultiChoiceReport {
    export type ChartType = "Pie" | "Bar" | undefined

    export type Entry = {
        type: "MultiChoice",
        forEntryWithId: string,
        template: string,
        doSummarise: boolean,
        chartType: ChartType,
    }

    export type Hint = ValidationHint

    export type UiState = {
        type: "MultiChoice",
        template: Writable<string>,
        doSummarise: Writable<boolean>,
        chartType: Writable<ChartType>,
        entry: Readable<Entry>,
        hints: Readable<Hint[]>,
    }

    export function fromSurveyEntry(entry: MultiChoice.Entry): Entry {
        return {
            type: "MultiChoice",
            forEntryWithId: entry.id,
            template: "",
            doSummarise: false,
            chartType: undefined,
        }
    }

    export function toUiState(initial: Entry): UiState {
        const template = writable(initial.template)
        const doSummarise = writable(initial.doSummarise)
        const chartType = writable(initial.chartType)
        const entry = derived(
            [template, doSummarise, chartType],
            ([$template, $doSummarise, $chartType]) => {
                return {
                    type: "MultiChoice",
                    forEntryWithId: initial.forEntryWithId,
                    template: $template,
                    doSummarise: $doSummarise,
                    chartType: $chartType,
                } satisfies Entry
            },
        )
        const hints = derived(entry, () => [])
        return {
            type: "MultiChoice",
            template: template,
            doSummarise: doSummarise,
            chartType: chartType,
            entry: entry,
            hints: hints,
        }
    }
}
