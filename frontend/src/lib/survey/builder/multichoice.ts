import type { Readable, Writable } from "svelte/store";
import { MultiChoice } from "$lib/survey/multichoice";
import type { ValidationHint } from "$lib/survey/validation";

export namespace MultiChoiceBuilder {
    export type Hint = {
        type: "MinChoiceNotMatched" | "MaxChoiceNotMatched" | "OtherMaxLengthExceeded" | "Required",
    } & ValidationHint

    export type UiState = {
        isRequired: Writable<boolean>,
        question: Writable<string>,
        options: Writable<string[]>,
        isAcceptingOther: Writable<boolean>,
        minSelected: Writable<number>,
        maxSelected: Writable<number>,
        entry: Readable<MultiChoice.Entry>,
        hints: Readable<Hint[]>,
    }

    export function uiState(prefix: string) {

    }
}