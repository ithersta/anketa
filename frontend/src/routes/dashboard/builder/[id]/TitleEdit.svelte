<script lang="ts">
    import { CheckIcon, Pencil } from "lucide-svelte";
    import { Button } from "$lib/components/ui/button/index.js";
    import type { SurveyDraft } from "$lib/builder/draft";
    import { db } from "$lib/db/db";
    import { Input } from "$lib/components/ui/input";

    export let draft: SurveyDraft
    let isEditingTitle = false
    let newTitle = draft.title

    function onEdit() {
        isEditingTitle = true
        newTitle = draft.title
    }

    async function onDone() {
        await db.surveyDrafts.update(draft.id, { title: newTitle })
        isEditingTitle = false
    }
</script>

<div class="flex space-x-2 pb-6">
    {#if isEditingTitle}
        <form on:submit={onDone}>
            <Input type="text" class="max-w-xs" bind:value={newTitle}/>
        </form>
        <Button variant="ghost" size="icon" on:click={onDone}>
            <CheckIcon class="h-4 w-4"/>
        </Button>
    {:else}
        <h2 class="text-3xl font-bold tracking-tight">{draft.title}</h2>
        <Button variant="ghost" size="icon" on:click={onEdit}>
            <Pencil class="h-4 w-4"/>
        </Button>
    {/if}
</div>
