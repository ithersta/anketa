<script lang="ts">
    import { Textarea } from "$lib/components/ui/textarea";
    import * as Dialog from "$lib/components/ui/dialog";
    import { MultiChoiceReport } from "$lib/report/multichoice";
    import { MultiChoice } from "$lib/survey/multichoice";
    import Summarization from "./Summarization.svelte";
    import FormattingSection from "./FormattingSection.svelte";
    import ChartTypeSelect from "./ChartTypeSelect.svelte";

    export let uiState: MultiChoiceReport.UiState
    export let forceError: boolean
    export let surveyEntry: MultiChoice.Entry
    const { template, doSummarise, chartType, hints } = uiState
    forceError

    let formattingProperties = surveyEntry.options
        .map((o, index) => `t${index + 1} – "${o}", c${index + 1} – количество ответов, pc${index + 1} – в процентах`)
        .join("\n")
</script>

<Dialog.Header>
    <Dialog.Title>{surveyEntry.question}</Dialog.Title>
    <Dialog.Description>Множественный выбор</Dialog.Description>
</Dialog.Header>
<Textarea bind:value={$template}></Textarea>
<FormattingSection {formattingProperties}/>
{#if surveyEntry.isAcceptingOther}
    <Summarization surveyEntry={surveyEntry} doSummarise={doSummarise}/>
{/if}
<ChartTypeSelect chartType={chartType} />
<!--<ValidationResult hints={$hints} {forceError} let:hint>-->
<!--    {#if (hint.type === "CannotBeEmpty")}-->
<!--        <span>Текст не может быть пустым</span>-->
<!--    {:else}-->
<!--        <UnknownHint/>-->
<!--    {/if}-->
<!--</ValidationResult>-->
