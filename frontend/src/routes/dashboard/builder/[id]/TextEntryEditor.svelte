<script lang="ts">
    import { Text } from "$lib/survey/text";
    import { Textarea } from "$lib/components/ui/textarea";
    import * as Dialog from "$lib/components/ui/dialog";
    import ValidationResult from "../../../survey/[id]/ValidationResult.svelte";
    import UnknownHint from "../../../survey/[id]/UnknownHint.svelte";

    export let uiState: Text.Builder.UiState
    export let forceError: boolean
    const { content, hints } = uiState
</script>

<Dialog.Header>
    <Dialog.Title>Текст</Dialog.Title>
</Dialog.Header>
<Textarea bind:value={$content}></Textarea>
<ValidationResult hints={$hints} {forceError} let:hint>
    {#if (hint.type === "CannotBeEmpty")}
        <span>Текст не может быть пустым</span>
    {:else}
        <UnknownHint/>
    {/if}
</ValidationResult>