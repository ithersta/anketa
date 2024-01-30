<script lang="ts">
    import { Text } from "$lib/survey/text";
    import { Textarea } from "$lib/components/ui/textarea";
    import ValidationResult from "../../../survey/[id]/ValidationResult.svelte";
    import UnknownHint from "../../../survey/[id]/UnknownHint.svelte";

    export let uiState: Text.Builder.UiState
    export let forceError: boolean
    const content = uiState.content
    const hints = uiState.hints
</script>

<Textarea bind:value={$content}></Textarea>
<ValidationResult hints={$hints} {forceError} let:hint>
    {#if (hint.type === "CannotBeEmpty")}
        <span>Текст не может быть пустым</span>
    {:else}
        <UnknownHint/>
    {/if}
</ValidationResult>