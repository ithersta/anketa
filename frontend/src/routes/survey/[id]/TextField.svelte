<script lang="ts">
    import * as Card from "$lib/components/ui/card";
    import { Textarea } from "$lib/components/ui/textarea";
    import { TextField } from "$lib/survey/textfield";
    import UnknownHint from "./UnknownHint.svelte";
    import ValidationResult from "./ValidationResult.svelte";
    import { persisted } from "svelte-persisted-store";
    import type { Writable } from "svelte/store";
    import type { SurveyAnswer } from "$lib/survey/entries";

    export let entry: TextField.Entry
    export let forceError: boolean
    export let post: (uuid: string, answer: SurveyAnswer) => void

    let answer: Writable<string> = persisted(`answer-${entry.id}`, "")

    $: nullifiedAnswer = (($answer?.length ?? 0) === 0) ? undefined : { type: "TextField", text: $answer }
    $: hints = TextField.validate(entry, nullifiedAnswer)
    $: { post(entry.id, nullifiedAnswer) }
</script>

<Card.Root>
    <Card.Header>
        <Card.Title>{entry.question}</Card.Title>
    </Card.Header>
    <Card.Content>
        <Textarea bind:value={$answer} id="textarea-{entry.id}"></Textarea>
    </Card.Content>
    <Card.Footer>
        <ValidationResult {hints} {forceError} let:hint>
            {#if (hint.type === "MinLengthNotMatched")}
                <span>Минимум {entry.minLength}</span>
            {:else if (hint.type === "MaxLengthExceeded")}
                <span>{$answer?.length}/{entry.maxLength}</span>
            {:else}
                <UnknownHint/>
            {/if}
        </ValidationResult>
    </Card.Footer>
</Card.Root>