<script lang="ts">
    import { Textarea } from "$lib/components/ui/textarea";
    import * as Dialog from "$lib/components/ui/dialog";
    import { PolarChoiceReport } from "$lib/report/polarchoice";
    import { PolarChoice } from "$lib/survey/polarchoice";
    import FormattingSection from "./FormattingSection.svelte";

    export let uiState: PolarChoiceReport.UiState
    export let forceError: boolean
    export let surveyEntry: PolarChoice.Entry
    const { template, hints } = uiState
    forceError

    let options = new Array(surveyEntry.range * 2 + 1).fill(null).map((_, i) => i - surveyEntry.range)
    let formattingProperties = options
        .map((o, index) => `t${index + 1} – "${o}", c${index + 1} – количество ответов, pc${index + 1} – в процентах`)
        .join("\n")
</script>

<Dialog.Header>
    <Dialog.Title>{surveyEntry.question}</Dialog.Title>
    <Dialog.Description>Анкета полярных профилей</Dialog.Description>
</Dialog.Header>
<Textarea bind:value={$template}></Textarea>
<FormattingSection {formattingProperties}/>
<!--<ValidationResult hints={$hints} {forceError} let:hint>-->
<!--    {#if (hint.type === "CannotBeEmpty")}-->
<!--        <span>Текст не может быть пустым</span>-->
<!--    {:else}-->
<!--        <UnknownHint/>-->
<!--    {/if}-->
<!--</ValidationResult>-->

