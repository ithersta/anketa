<script lang="ts">
    import * as Dialog from "$lib/components/ui/dialog";
    import { ScrollArea } from "$lib/components/ui/scroll-area/index.js";
    import { TextFieldReport } from "$lib/report/textfield";
    import { TextField } from "$lib/survey/textfield";
    import { Button } from "$lib/components/ui/button";
    import { SparklesIcon } from "lucide-svelte";
    import { page } from "$app/stores";
    import { db } from "$lib/db/db.js";
    import { liveQuery } from "dexie";
    import { Switch } from "$lib/components/ui/switch";
    import { Label } from "$lib/components/ui/label";

    export let uiState: TextFieldReport.UiState
    export let surveyEntry: TextField.Entry
    export let forceError: boolean
    const { content, doSummarise, hints } = uiState
    const surveyId = $page.params.id
    const summarization = liveQuery(async () => {
        return db.summarizations
            .get(surveyEntry.id)
    })
    let requestCount = 0

    async function generateSummary() {
        if (requestCount != 0) return
        requestCount++
        try {
            const response = await fetch(`/dashboard/survey/${surveyId}/export/summarise/${surveyEntry.id}`, {
                method: "POST",
            })
            if (response.ok) {
                await db.summarizations.update(surveyEntry.id, {
                    entryId: surveyEntry.id,
                    content: await response.text(),
                })
            }
        } finally {
            requestCount--
        }
    }
</script>

<Dialog.Header>
    <Dialog.Title>{surveyEntry.question}</Dialog.Title>
    <Dialog.Description>Текстовое поле</Dialog.Description>
</Dialog.Header>
<div class="flex items-center space-x-2">
    <Switch id="use-summary" bind:checked={$doSummarise}/>
    <Label for="use-summary">Использовать краткий пересказ</Label>
</div>
<Button variant="secondary" on:click={generateSummary} disabled={requestCount !== 0}>
    <SparklesIcon class="mr-2 h-4 w-4"/>
    Сгенерировать краткий пересказ
</Button>
{#if $summarization !== undefined}
    <ScrollArea class="h-72 rounded-md border">
        <div class="p-4">
            <span class="summary">{$summarization.content}</span>
        </div>
    </ScrollArea>
{/if}

<style>
    .summary {
        white-space: pre-line;
    }
</style>
