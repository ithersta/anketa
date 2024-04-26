<script lang="ts">
    import { slide } from "svelte/transition"
    import { Textarea } from "$lib/components/ui/textarea";
    import * as Dialog from "$lib/components/ui/dialog";
    import ValidationResult from "../../../survey/[id]/ValidationResult.svelte";
    import UnknownHint from "../../../survey/[id]/UnknownHint.svelte";
    import { Label } from "$lib/components/ui/label";
    import { Input } from "$lib/components/ui/input";
    import IsRequired from "./IsRequired.svelte";
    import { MultiChoice } from "$lib/survey/multichoice";
    import { Checkbox } from "$lib/components/ui/checkbox";
    import { Button } from "$lib/components/ui/button";
    import { Plus, XIcon } from "lucide-svelte";

    export let uiState: MultiChoice.Builder.UiState
    export let forceError: boolean
    const {
        question, minSelected, maxSelected,
        options, isAcceptingOther, otherMaxLength,
        hints, isRequired, entry
    } = uiState

    let newOption = ""

    function addOption() {
        if (newOption.length === 0) return
        options.update(o => {
            const id = o.length === 0 ? 0 : o[o.length - 1].id + 1
            o.push({ id: id, text: newOption })
            return o
        })
        newOption = ""
    }

    function deleteOption(id: number) {
        options.update(o => {
            const index = o.findLastIndex(value => value.id === id)
            o.splice(index, 1)
            return o
        })
    }
</script>

<Dialog.Header>
    <Dialog.Title>
        {#if MultiChoice.isRadio($entry)}
            Одиночный выбор
        {:else}
            Множественный выбор
        {/if}
    </Dialog.Title>
</Dialog.Header>
<div class="grid w-full gap-2">
    <Label for="question">Вопрос</Label>
    <Textarea bind:value={$question} id="question"></Textarea>
</div>
<div class="grid w-full">
    <Label class="pb-2">Варианты ответа</Label>
    {#each $options as option (option.id)}
        <div class="flex flex-row gap-2 items-center py-1" transition:slide>
            <Label class="flex-grow">{option.text}</Label>
            <Button variant="ghost" size="icon" on:click={() => deleteOption(option.id)}>
                <XIcon class="h-4 w-4"/>
            </Button>
        </div>
    {/each}
    <div class="flex flex-row items-center gap-2 pt-2">
        <form on:submit={addOption} class="flex-grow">
            <Input bind:value={newOption}/>
        </form>
        <Button variant="secondary" on:click={addOption}>
            Добавить вариант ответа
        </Button>
    </div>
</div>
<div class="grid w-full grid-cols-2 gap-4">
    <div class="grid w-full gap-2">
        <Label for="min-selected">Минимальное количество ответов</Label>
        <Input bind:value={$minSelected} id="min-selected"></Input>
    </div>
    <div class="grid w-full gap-2">
        <Label for="max-selected">Максимальное количество ответов</Label>
        <Input bind:value={$maxSelected} id="max-selected"></Input>
    </div>
</div>
<div class="flex items-center space-x-2">
    <Checkbox id="accept-other" bind:checked={$isAcceptingOther}/>
    <Label for="accept-other" class="text-sm font-medium leading-none">Ответ «другое»</Label>
</div>
{#if $isAcceptingOther}
    <div class="grid w-full gap-2" transition:slide>
        <Label for="max-other">Максимальная длина ответа</Label>
        <Input bind:value={$otherMaxLength} id="max-other"></Input>
    </div>
{/if}
<IsRequired bind:isRequired={$isRequired}/>
<ValidationResult hints={$hints} {forceError} let:hint>
    {#if hint.type === "InvalidOptionsRange"}
        <span>Некорректные ограничения количества ответов</span>
    {:else if hint.type === "InvalidMinSelected"}
        <span>Некорректное минимальное количество ответов</span>
    {:else if hint.type === "InvalidMaxSelected"}
        <span>Некорректное максимальное количество ответов</span>
    {:else if hint.type === "EmptyQuestion"}
        <span>Вопрос не может быть пустым</span>
    {:else if hint.type === "OptionsEmpty"}
        <span>Варианты ответа не могут быть пустыми</span>
    {:else if hint.type === "InvalidOtherMaxLength"}
        <span>Некорректная длина ответа «другое»</span>
    {:else}
        <UnknownHint/>
    {/if}
</ValidationResult>
