<script lang="ts">
    import { Textarea } from "$lib/components/ui/textarea";
    import ValidationResult from "../../../survey/[id]/ValidationResult.svelte";
    import UnknownHint from "../../../survey/[id]/UnknownHint.svelte";
    import { TextField } from "$lib/survey/textfield";
    import { Label } from "$lib/components/ui/label";
    import { Input } from "$lib/components/ui/input";

    export let uiState: TextField.Builder.UiState
    export let forceError: boolean
    const question = uiState.question
    const minLength = uiState.minLength
    const maxLength = uiState.maxLength
    const hints = uiState.hints
</script>

<div class="grid w-full gap-1.5">
    <Label for="question">Вопрос</Label>
    <Textarea bind:value={$question} id="question"></Textarea>
</div>
<div class="grid w-full grid-cols-2 gap-2">
    <div class="grid w-full gap-1.5">
        <Label for="min-length">Минимальная длина ответа</Label>
        <Input bind:value={$minLength} id="min-length"></Input>
    </div>
    <div class="grid w-full gap-1.5">
        <Label for="max-length">Максимальная длина ответа</Label>
        <Input bind:value={$maxLength} id="max-length"></Input>
    </div>
</div>
<ValidationResult hints={$hints} {forceError} let:hint>
    {#if hint.type === "MinLengthExceedsMaxLength"}
        <span>Минимальная длина не может быть больше максимальной</span>
    {:else if hint.type === "InvalidMinLength"}
        <span>Некорректная минимальная длина</span>
    {:else if hint.type === "InvalidMaxLength"}
        <span>Некорректная максимальная длина</span>
    {:else if hint.type === "EmptyQuestion"}
        <span>Вопрос не может быть пустым</span>
    {:else}
        <UnknownHint/>
    {/if}
</ValidationResult>