<script lang="ts">
    import { slide } from 'svelte/transition';
    import { liveQuery } from "dexie";
    import { db } from "$lib/db/db";
    import { page } from "$app/stores";
    import NewEntry from "./NewEntry.svelte";
    import { Button } from "$lib/components/ui/button";
    import TitleEdit from "./TitleEdit.svelte";
    import type { EntryEditorDialogState } from "./EntryEditorDialogState";
    import EntryEditorDialog from "./EntryEditorDialog.svelte";
    import { type SurveyEntryBuilderUiState, toBuilderUiState, toPreviewUiState, toUiState } from "$lib/survey/entries";
    import SurveyEntryComponent from "../../../survey/[id]/SurveyEntryComponent.svelte";
    import { Pencil, Trash } from "lucide-svelte";
    import type { SurveyDraftEntry } from "$lib/builder/draft";
    import type { SurveyContent } from "$lib/survey/survey";
    import { goto } from "$app/navigation";
    import DeleteDialog from "./DeleteDialog.svelte";

    const id = Number.parseInt($page.params.id)

    const draft = liveQuery(async () => {
        return db.surveyDrafts.get(id)
    })

    const entries = liveQuery(async () => {
        return db.surveyDraftEntries
            .where("surveyId")
            .equals(id)
            .sortBy("order")
    })

    let entryEditorState: EntryEditorDialogState = undefined
    let isDeleteDialogOpen = false

    function openEditorExisting(entry: SurveyDraftEntry) {
        entryEditorState = {
            uiState: toBuilderUiState(entry.content),
            id: entry.id,
        }
    }

    function openEditor(uiState: SurveyEntryBuilderUiState) {
        entryEditorState = { uiState: uiState }
    }

    function closeEditor() {
        entryEditorState = undefined
    }

    function openDeleteDialog() {
        isDeleteDialogOpen = true
    }

    async function create() {
        const title = (await db.surveyDrafts.get(id))?.title
        const entries = (await db.surveyDraftEntries
            .where("surveyId")
            .equals(id)
            .sortBy("order"))
            .map(e => e.content)
        if (!title || !entries) return
        const survey = {
            title: title,
            entries: entries,
        } satisfies SurveyContent
        const response = await fetch("/dashboard/builder", {
            method: "POST",
            body: JSON.stringify(survey),
        })
        if (response.ok) {
            await deleteDraft()
            const createdSurveyId = await response.text()
            await goto(`/dashboard/survey/${createdSurveyId}`)
        }
    }

    async function deleteDraft() {
        await db.transaction("rw", [db.surveyDrafts, db.surveyDraftEntries], async () => {
            await db.surveyDraftEntries
                .where("surveyId")
                .equals(id)
                .delete()
            await db.surveyDrafts
                .delete(id)
        })
    }

    async function deleteAndExit() {
        await deleteDraft()
        await goto("/dashboard")
    }

</script>

<svelte:head>
    <title>{$draft?.title ?? "Новая анкета"}</title>
</svelte:head>

<DeleteDialog bind:dialogOpen={isDeleteDialogOpen} deleteDraft={deleteAndExit}/>

{#if $draft}
    <EntryEditorDialog bind:state={entryEditorState} surveyId={id} close={closeEditor}/>
    <div class="max-w-prose mx-auto p-4">
        <div class="flex flex-row">
            <TitleEdit draft={$draft}/>
            <Button variant="ghost" size="icon" on:click={openDeleteDialog}>
                <Trash class="h-4 w-4"/>
            </Button>
        </div>
        {#each ($entries || []) as entry (JSON.stringify(entry))}
            <div class="py-2 flex flex-row space-x-2" transition:slide>
                <div class="pointer-events-none cursor-default flex-grow">
                    <SurveyEntryComponent
                            uiState={toPreviewUiState(entry.content)}
                            forceError={false}/>
                </div>
                <div class="flex flex-col">
                    <Button variant="ghost" size="icon" on:click={() => openEditorExisting(entry)}>
                        <Pencil class="h-4 w-4"/>
                    </Button>
                </div>
            </div>
        {/each}
        <div class="py-2 pe-12">
            <NewEntry {openEditor}/>
        </div>
        <div class="py-2 pe-12 flex flex-row justify-end">
            <Button on:click={create}>Создать</Button>
        </div>
    </div>
{/if}
