import type { ValidationHint } from "$lib/survey/validation";
import { requiredHint } from "$lib/survey/validation";
import { derived, type Readable, type Writable } from "svelte/store";
import { persisted } from "svelte-persisted-store";

export namespace TextField {
    export type Entry = {
        type: "TextField",
        id: string,
        isRequired: boolean,
        question: string,
        minLength: number,
        maxLength: number,
    }

    export type Answer = {
        type: "TextField",
        text: string,
    }

    export type Hint = {
        type: "MaxLengthExceeded" | "MinLengthNotMatched" | "Required"
    } & ValidationHint

    export type UiState = {
        entry: Entry,
        text: Writable<string>,
        answer: Readable<Answer | undefined>,
        hints: Readable<Hint[]>,
        clear: () => void,
    }

    export function toUiState(entry: Entry, prefix: string): UiState {
        const text: Writable<string> = persisted(`${prefix}${entry.id}`, "")
        const answer: Readable<Answer | undefined> = derived(
            text,
            ($text) => {
                if ($text.length === 0) return undefined
                return {
                    type: "TextField",
                    text: $text,
                } satisfies TextField.Answer
            }
        )
        const hints: Readable<Hint[]> = derived(
            answer,
            ($answer) => validate(entry, $answer)
        )
        return {
            entry: entry,
            text: text,
            answer: answer,
            hints: hints,
            clear: () => { text.set("") },
        }
    }

    function validate(entry: Entry, answer: Answer | undefined): Hint[] {
        if (answer === undefined && !entry.isRequired) return []
        let length = answer?.text?.length ?? 0
        return [
            requiredHint(entry, answer),
            {
                type: "MaxLengthExceeded",
                isHint: length >= entry.minLength,
                isError: length > entry.maxLength
            },
            {
                type: "MinLengthNotMatched",
                isHint: length < entry.minLength,
                isError: answer !== undefined && length < entry.minLength,
            },
        ]
    }
}