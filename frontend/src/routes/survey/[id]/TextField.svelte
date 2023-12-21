<script lang="ts">
    import * as Card from "$lib/components/ui/card";
    import { Textarea } from "$lib/components/ui/textarea";
    import { TextField } from "$lib/survey/textfield";
    import UnknownHint from "./UnknownHint.svelte";
    import ValidationResult from "./ValidationResult.svelte";

    export let uiState: TextField.UiState
    export let forceError: boolean

    let { entry, text, hints } = uiState
</script>

<Card.Root>
    <Card.Header>
        <Card.Title>{entry.question}</Card.Title>
    </Card.Header>
    <Card.Content>
        <Textarea bind:value={$text} id="textarea-{entry.id}"></Textarea>
    </Card.Content>
    <Card.Footer>
        <ValidationResult hints={$hints} {forceError} let:hint>
            {#if (hint.type === "MinLengthNotMatched")}
                <span>Минимум {entry.minLength}</span>
            {:else if (hint.type === "MaxLengthExceeded")}
                <span>{$text?.length}/{entry.maxLength}</span>
            {:else}
                <UnknownHint/>
            {/if}
        </ValidationResult>
    </Card.Footer>
</Card.Root>