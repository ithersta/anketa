<script lang="ts">
    import { liveQuery } from "dexie";
    import { db } from "$lib/db/db";
    import { page } from "$app/stores";
    import NewEntry from "./NewEntry.svelte";
    import { Button } from "$lib/components/ui/button";
    import TitleEdit from "./TitleEdit.svelte";
    import type { EntryEditorDialogState } from "./EntryEditorDialogState";
    import EntryEditorDialog from "./EntryEditorDialog.svelte";
    import { type SurveyEntryBuilderUiState, toUiState } from "$lib/survey/entries";
    import SurveyEntryComponent from "../../../survey/[id]/SurveyEntryComponent.svelte";

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
    <EntryEditorDialog state={entryEditorState} surveyId={id} close={closeEditor}/>
    <div class="max-w-prose mx-auto p-4">
        <TitleEdit draft={$draft}/>
        {#each ($entries || []) as entry (entry.id)}
            <div class="py-2">
                <SurveyEntryComponent uiState={toUiState(entry.content, "")} forceError={false}/>
            </div>
        {/each}
        <div class="py-2">
            <NewEntry {openEditor}/>
        </div>
    </div>
{/if}
