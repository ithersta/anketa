<script lang="ts">
    import { Textarea } from "$lib/components/ui/textarea";
    import * as Dialog from "$lib/components/ui/dialog";
    import ValidationResult from "../../../survey/[id]/ValidationResult.svelte";
    import UnknownHint from "../../../survey/[id]/UnknownHint.svelte";
    import { TextField } from "$lib/survey/textfield";
    import { Label } from "$lib/components/ui/label";
    import { Input } from "$lib/components/ui/input";
    import IsRequired from "./IsRequired.svelte";
    import { PolarChoice } from "$lib/survey/polarchoice";

    export let uiState: PolarChoice.Builder.UiState
    export let forceError: boolean
    const { question, range, hints, isRequired } = uiState
</script>

<Dialog.Header>
    <Dialog.Title>Анкета полярных профилей</Dialog.Title>
</Dialog.Header>
<div class="grid w-full gap-2">
    <Label for="question">Вопрос</Label>
    <Textarea bind:value={$question} id="question"></Textarea>
</div>
<div class="grid w-full gap-2">
    <Label for="range">Максимальное значение шкалы</Label>
    <Input bind:value={$range} id="range"></Input>
</div>
<IsRequired bind:isRequired={$isRequired}/>
<ValidationResult hints={$hints} {forceError} let:hint>
    {#if hint.type === "InvalidRange"}
        <span>Некорректная шкала</span>
    {:else if hint.type === "EmptyQuestion"}
        <span>Вопрос не может быть пустым</span>
    {:else}
        <UnknownHint/>
    {/if}
</ValidationResult>