<script lang="ts">
    import { Text } from "$lib/survey/text";
    import { Textarea } from "$lib/components/ui/textarea";
    import * as Dialog from "$lib/components/ui/dialog";
    import ValidationResult from "../../../../../../survey/[id]/ValidationResult.svelte";
    import UnknownHint from "../../../../../../survey/[id]/UnknownHint.svelte";
    import { MultiChoiceReport } from "$lib/report/multichoice";
    import { PolarChoiceReport } from "$lib/report/polarchoice";
    import { PolarChoice } from "$lib/survey/polarchoice";
    import { ScrollArea } from "$lib/components/ui/scroll-area";
    import { Label } from "$lib/components/ui/label";

    export let uiState: PolarChoiceReport.UiState
    export let forceError: boolean
    export let surveyEntry: PolarChoice.Entry
    const { template, hints } = uiState

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
