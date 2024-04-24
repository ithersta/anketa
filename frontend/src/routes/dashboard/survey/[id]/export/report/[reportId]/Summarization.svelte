<script lang="ts">
    import {SparklesIcon} from "lucide-svelte";
    import {ScrollArea} from "$lib/components/ui/scroll-area/index.js";
    import {Button} from "$lib/components/ui/button/index.js";
    import {Switch} from "$lib/components/ui/switch/index.js";
    import {Label} from "$lib/components/ui/label/index.js";
    import { db } from "$lib/db/db";
    import { page } from "$app/stores";
    import { liveQuery } from "dexie";
    import type { SurveyEntry } from "$lib/survey/entries";
    import type { Writable } from "svelte/store";
    import { safeFetch } from "$lib/safeFetch";

    export let surveyEntry: SurveyEntry
    export let doSummarise: Writable<boolean>
    const surveyId = $page.params.id
    const summarization = liveQuery(async () => {
        return db.summarizations
            .get(surveyEntry.id)
    })
    let requestCount = 0

    async function generateSummary() {
        if (requestCount != 0) return
        requestCount++
        const response = await safeFetch(`/dashboard/survey/${surveyId}/export/summarise/${surveyEntry.id}`, {
            method: "POST",
        })
        if (response && response.ok) {
            await db.summarizations.update(surveyEntry.id, {
                entryId: surveyEntry.id,
                content: await response.text(),
            })
        }
        requestCount--
    }
</script>

<div class="flex items-center space-x-2">
    <Switch id="use-summary" bind:checked={$doSummarise}/>
    <Label for="use-summary">Использовать краткий пересказ</Label>
</div>
<Button variant="secondary" on:click={generateSummary} disabled={requestCount !== 0}>
    <SparklesIcon class="mr-2 h-4 w-4"/>
    Сгенерировать краткий пересказ
</Button>
<ScrollArea class="h-72 rounded-md border">
    <div class="p-4">
        {#if $summarization !== undefined}
            <span class="summary">{$summarization.content}</span>
        {:else}

        {/if}
    </div>
</ScrollArea>

<style>
    .summary {
        white-space: pre-line;
    }
</style>
