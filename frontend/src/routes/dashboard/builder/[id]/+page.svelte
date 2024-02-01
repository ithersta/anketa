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
    import { type SurveyEntryBuilderUiState, toBuilderUiState, toUiState } from "$lib/survey/entries";
    import SurveyEntryComponent from "../../../survey/[id]/SurveyEntryComponent.svelte";
    import { Pencil, Trash } from "lucide-svelte";
    import type { SurveyDraftEntry } from "$lib/builder/draft";

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
</script>

<svelte:head>
    <title>{$draft?.title ?? "Новая анкета"}</title>
</svelte:head>

{#if $draft}
    <EntryEditorDialog bind:state={entryEditorState} surveyId={id} close={closeEditor}/>
    <div class="max-w-prose mx-auto p-4">
        <TitleEdit draft={$draft}/>
        {#each ($entries || []) as entry (entry.id)}
            <div class="py-2 flex flex-row space-x-2" transition:slide>
                <div class="pointer-events-none cursor-default flex-grow">
                    <SurveyEntryComponent
                            uiState={toUiState(entry.content, "draft")}
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
    </div>
{/if}
