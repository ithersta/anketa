<script lang="ts">
    import { Text } from "$lib/survey/text";
    import { Textarea } from "$lib/components/ui/textarea";
    import * as Dialog from "$lib/components/ui/dialog";
    import { MultiChoiceReport } from "$lib/report/multichoice";
    import { MultiChoice } from "$lib/survey/multichoice";
    import Summarization from "./Summarization.svelte";
    import { ScrollArea } from "$lib/components/ui/scroll-area";
    import { Label } from "$lib/components/ui/label";
    import * as Tooltip from "$lib/components/ui/tooltip/index.js";

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
<div class="flex flex-row items-baseline">
    <span class="text-lg font-semibold leading-none tracking-tight">Форматирование</span>
    <Tooltip.Root openDelay={0} class="flex-grow">
        <Tooltip.Trigger>
            <p class="text-sm text-muted-foreground text-end pl-4">Пример шаблона</p>
        </Tooltip.Trigger>
        <Tooltip.Content>
            Варианты ответа $&lbrace;t1&rbrace; и $&lbrace;t2&rbrace; суммарно выбрали $&lbrace;с1 + с2&rbrace; чел.
        </Tooltip.Content>
    </Tooltip.Root>
</div>
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
