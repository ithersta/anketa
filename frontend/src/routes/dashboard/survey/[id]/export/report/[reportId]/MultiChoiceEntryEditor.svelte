<script lang="ts">
    import { Text } from "$lib/survey/text";
    import { Textarea } from "$lib/components/ui/textarea";
    import * as Dialog from "$lib/components/ui/dialog";
    import { MultiChoiceReport } from "$lib/report/multichoice";
    import { MultiChoice } from "$lib/survey/multichoice";
    import Summarization from "./Summarization.svelte";
    import { ScrollArea } from "$lib/components/ui/scroll-area";

    export let uiState: MultiChoiceReport.UiState
    export let forceError: boolean
    export let surveyEntry: MultiChoice.Entry
    const { template, doSummarise, hints } = uiState

    let formattingProperties = surveyEntry.options
        .map((o, index) => `t${index + 1} – "${o}", c${index + 1} – количество ответов, pc${index + 1} – в процентах`)
        .join("\n")
</script>

<Dialog.Header>
    <Dialog.Title>{surveyEntry.question}</Dialog.Title>
    <Dialog.Description>Множественный выбор</Dialog.Description>
</Dialog.Header>
<Textarea bind:value={$template}></Textarea>
<span class="text-lg font-semibold leading-none tracking-tight">Форматирование</span>
<ScrollArea class="h-48 rounded-md border">
    <div class="p-4">
        <span class="format-hint text-muted-foreground text-sm">
            {formattingProperties}
            ac – общее количество ответов
            nac – количество воздержавшихся
        </span>
    </div>
</ScrollArea>
{#if surveyEntry.isAcceptingOther}
    <Summarization surveyEntry={surveyEntry} doSummarise={doSummarise}/>
{/if}
<!--<ValidationResult hints={$hints} {forceError} let:hint>-->
<!--    {#if (hint.type === "CannotBeEmpty")}-->
<!--        <span>Текст не может быть пустым</span>-->
<!--    {:else}-->
<!--        <UnknownHint/>-->
<!--    {/if}-->
<!--</ValidationResult>-->

<style>
    .format-hint {
        white-space: pre-line;
    }
</style>
