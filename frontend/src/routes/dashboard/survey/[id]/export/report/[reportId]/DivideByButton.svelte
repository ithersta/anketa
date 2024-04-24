<script lang="ts">
    import { getShortName } from "$lib/survey/entries";
    import type { SurveyContent } from "$lib/survey/survey";
    import type { ReportDraft } from "$lib/report/draft";
    import { Button } from "$lib/components/ui/button";
    import { XIcon } from "lucide-svelte";

    export let openDivideByDialog: () => void
    export let clear: () => void
    export let survey: SurveyContent
    export let draft: ReportDraft
    $: divideByEntry = survey.entries.find(e => e.id === draft.divideBy)
</script>

<h1 class="text-lg font-semibold leading-none tracking-tight pb-4 pt-8">Дробление отчёта</h1>
<div class="py-2 flex flex-row space-x-2 pe-12">
    <Button on:click={openDivideByDialog} variant="secondary" class="flex-grow">
        {#if divideByEntry !== undefined}
            По ответу на вопрос «{getShortName(divideByEntry)}»
        {:else}
            Выбрать вопрос
        {/if}
    </Button>
    {#if divideByEntry !== undefined}
        <Button variant="ghost" size="icon" on:click={clear}>
            <XIcon class="h-4 w-4"/>
        </Button>
    {/if}
</div>
