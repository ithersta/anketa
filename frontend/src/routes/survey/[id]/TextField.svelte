<script lang="ts">
    import * as Card from "$lib/components/ui/card";
    import { Label } from "$lib/components/ui/label";
    import { Textarea } from "$lib/components/ui/textarea";
    import { TextField } from "$lib/survey/textfield";
    import UnknownHint from "./UnknownHint.svelte";
    import ValidationResult from "./ValidationResult.svelte";

    export let entry: TextField.Entry
    export let answer: TextField.Answer
    export let forceError: boolean

    $: nullifiedAnswer = ((answer?.length ?? 0) === 0) ? undefined : answer
    $: length = answer?.length ?? 0
    $: hints = TextField.validate(entry, nullifiedAnswer)
</script>

<Card.Root>
    <Card.Header>
        <Card.Title>{entry.question}</Card.Title>
    </Card.Header>
    <Card.Content>
        <Textarea bind:value={answer} id="textarea-{entry.id}"></Textarea>
    </Card.Content>
    <Card.Footer>
        <ValidationResult {hints} {forceError} let:hint>
            {#if (hint.type === "MinLengthNotMatched")}
                <span>Минимум {entry.minLength}</span>
            {:else if (hint.type === "MaxLengthExceeded")}
                <span>{length}/{entry.maxLength}</span>
            {:else if (hint.type === "Required")}
                <span>Требуется ответ</span>
            {:else}
                <UnknownHint/>
            {/if}
        </ValidationResult>
    </Card.Footer>
</Card.Root>