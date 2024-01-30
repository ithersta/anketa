import type { ValidationHint } from "$lib/survey/validation";
import { requiredHint } from "$lib/survey/validation";
import { derived, type Readable, writable, type Writable } from "svelte/store";
import { persisted } from "svelte-persisted-store";
import { NilUUID } from "$lib/uuid";
import { parseIntStrict } from "$lib/parseIntStrict";

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
        type: "MaxLengthExceeded" | "MinLengthNotMatched" | "Required",
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
            },
        )
        const hints: Readable<Hint[]> = derived(answer, ($answer) => validate(entry, $answer))
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
                isError: length > entry.maxLength,
            },
            {
                type: "MinLengthNotMatched",
                isHint: length < entry.minLength,
                isError: answer !== undefined && length < entry.minLength,
            },
        ]
    }

    export namespace Builder {
        export type Hint = {
            type: "MinLengthExceedsMaxLength" | "InvalidMinLength" | "InvalidMaxLength" | "EmptyQuestion",
        } & ValidationHint

        export type UiState = {
            type: "TextField",
            isRequired: Writable<boolean>,
            question: Writable<string>,
            minLength: Writable<string>,
            maxLength: Writable<string>,
            entry: Readable<Entry>,
            hints: Readable<Hint[]>,
        }

        export function toUiState(initial?: Entry): UiState {
            const isRequired = writable(initial?.isRequired ?? true)
            const question = writable(initial?.question ?? "")
            const minLength = writable(initial?.minLength?.toString() ?? "0")
            const maxLength = writable(initial?.maxLength?.toString() ?? "500")
            const entry = derived(
                [isRequired, question, minLength, maxLength],
                ([$isRequired, $question, $minLength, $maxLength]) => {
                    return {
                        type: "TextField",
                        id: NilUUID,
                        isRequired: $isRequired,
                        question: $question,
                        minLength: parseIntStrict($minLength),
                        maxLength: parseIntStrict($maxLength),
                    } satisfies Entry
                },
            )
            const hints = derived(entry, ($entry) => validate($entry))
            return {
                type: "TextField",
                isRequired: isRequired,
                question: question,
                minLength: minLength,
                maxLength: maxLength,
                entry: entry,
                hints: hints,
            }
        }

        function validate(entry: Entry): Hint[] {
            return [
                {
                    type: "MinLengthExceedsMaxLength",
                    isError: entry.maxLength < entry.minLength,
                    isHint: false,
                },
                {
                    type: "InvalidMinLength",
                    isError: !Number.isInteger(entry.minLength) || entry.minLength < 0,
                    isHint: false,
                },
                {
                    type: "InvalidMaxLength",
                    isError: !Number.isInteger(entry.maxLength) || entry.maxLength < 0,
                    isHint: false,
                },
                {
                    type: "EmptyQuestion",
                    isError: entry.question.length === 0,
                    isHint: false,
                },
            ]
        }
    }
}