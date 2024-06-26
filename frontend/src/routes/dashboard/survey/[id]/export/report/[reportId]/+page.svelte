<script lang="ts">
    import { page } from "$app/stores";
    import { liveQuery } from "dexie";
    import { db } from "$lib/db/db";
    import { goto } from "$app/navigation";
    import DeleteDialog from "./DeleteDialog.svelte";
    import EntryEditorDialog from "./EntryEditorDialog.svelte";
    import type { EntryEditorDialogState } from "./EntryEditorDialogState";
    import { type SurveyEntry } from "$lib/survey/entries";
    import { Pencil, Trash } from "lucide-svelte";
    import { fromSurveyEntry, type ReportEntryUiState, toUiState } from "$lib/report/entries";
    import TitleEdit from "./TitleEdit.svelte";
    import { Button } from "$lib/components/ui/button";
    import { slide } from "svelte/transition";
    import type { ReportContent, ReportDraftEntry } from "$lib/report/draft";
    import ReportComponent from "./ReportComponent.svelte";
    import NewEntry from "./NewEntry.svelte";
    import SurveyEntryChooser from "./SurveyEntryChooser.svelte";
    import DivideByButton from "./DivideByButton.svelte";
    import { safeFetch } from "$lib/safeFetch";

    export let data
    const survey = data.survey

    const surveyId = $page.params.id
    const id = Number.parseInt($page.params.reportId)

    const draftExists = liveQuery(async () => {
        let count = await db.reportDrafts
            .where("id")
            .equals(id)
            .count()
        return count > 0
    })

    $: {
        if ($draftExists === false) {
            goto(`/dashboard/survey/${surveyId}`)
        }
    }

    const draft = liveQuery(async () => {
        return db.reportDrafts.get(id);
    })!

    const entries = liveQuery(async () => {
        return db.reportDraftEntries
            .where("draftId")
            .equals(id)
            .sortBy("order")
    })

    let entryEditorState: EntryEditorDialogState = undefined
    let isDeleteDialogOpen = false
    let isChooseNewEntryDialogOpen = false
    let isDivideByDialogOpen = false

    function openDeleteDialog() {
        isDeleteDialogOpen = true
    }

    function openChooseNewEntryDialog() {
        isChooseNewEntryDialogOpen = true
    }

    function openDivideByDialog() {
        isDivideByDialogOpen = true
    }

    function openNewEditor(surveyEntry: SurveyEntry) {
        isChooseNewEntryDialogOpen = false
        openEditor(toUiState(fromSurveyEntry(surveyEntry)!!))
    }

    async function setDivideBy(surveyEntry: SurveyEntry) {
        isDivideByDialogOpen = false
        await db.reportDrafts.update(id, {
            divideBy: surveyEntry.id,
        })
    }

    async function resetDivideBy() {
        await db.reportDrafts.update(id, {
            divideBy: undefined,
        })
    }

    async function deleteDraft() {
        await db.transaction("rw", [db.reportDrafts, db.reportDraftEntries], async () => {
            await db.reportDraftEntries
                .where("draftId")
                .equals(id)
                .delete()
            await db.reportDrafts
                .delete(id)
        })
    }

    function openEditorExisting(entry: ReportDraftEntry) {
        entryEditorState = {
            uiState: toUiState(entry.content),
            id: entry.id,
        }
    }

    function openEditor(uiState: ReportEntryUiState) {
        entryEditorState = { uiState: uiState }
    }

    function closeEditor() {
        entryEditorState = undefined
    }

    async function generate() {
        const draftValue = draft.getValue?.()
        const entriesValue = entries.getValue?.()
        const reportContent: ReportContent = {
            entries: entriesValue?.map(e => e.content),
            divideBy: draftValue?.divideBy,
        }
        let response = await safeFetch(`/dashboard/survey/${draftValue?.surveyId}/export/report`, {
            method: "POST",
            body: JSON.stringify(reportContent),
        })
        if (response && response.ok) {
            const fileURL = URL.createObjectURL(await response.blob())
            const fileLink = document.createElement('a')
            const extension = response.headers.get("x-extension")!
            const filename = `${survey.title}-${new Date().toISOString()}.${extension}`
            fileLink.href = fileURL
            fileLink.download = filename
            fileLink.click()
        }
    }

    let dragStartIndex: number
    let dragEnterIndex: number
    let dropIndex: number

    async function handleDrop() {
        if (dragStartIndex === dropIndex) return
        const entriesValue = entries.getValue!()
        const draggedItem = entriesValue[dragStartIndex]
        const newEntries = [...entriesValue]
        newEntries.splice(dragStartIndex, 1)
        newEntries.splice(dropIndex, 0, draggedItem)
        const updates = newEntries.map((e, index) => {
            return {
                key: e.id,
                changes: {
                    order: index,
                },
            }
        })
        await db.reportDraftEntries.bulkUpdate(updates)
    }

    function handleDragOver(e) {
        const targetTop = e.target.getBoundingClientRect().top
        const targetHeight = e.target.getBoundingClientRect().height
        const yLoc = e.clientY - targetTop
        if (yLoc < targetHeight / 2) {
            dropIndex = dragEnterIndex
        } else {
            dropIndex = dragEnterIndex + 1
        }
    }

    function handleDragStart(index: number) {
        dragStartIndex = index
    }

    function handleDragEnter(index: number) {
        dragEnterIndex = index
    }
</script>

<svelte:head>
    <title>{$draft?.title ?? "Новый отчёт"}</title>
</svelte:head>

<DeleteDialog bind:dialogOpen={isDeleteDialogOpen} deleteDraft={deleteDraft}/>
<SurveyEntryChooser bind:dialogOpen={isChooseNewEntryDialogOpen} entries={survey.entries.filter(e => fromSurveyEntry(e) !== undefined)} onSelect={openNewEditor}/>
<SurveyEntryChooser bind:dialogOpen={isDivideByDialogOpen} entries={survey.entries.filter(e => fromSurveyEntry(e) !== undefined)} onSelect={setDivideBy}/>

{#if $draft}
    <EntryEditorDialog bind:state={entryEditorState} draftId={id} close={closeEditor} {survey}/>
    <div class="max-w-prose mx-auto p-4">
        <div class="flex flex-row">
            <TitleEdit draft={$draft}/>
            <Button variant="ghost" size="icon" on:click={openDeleteDialog}>
                <Trash class="h-4 w-4"/>
            </Button>
        </div>
        <div role="list"
            on:drop={handleDrop}
            on:dragover|preventDefault={handleDragOver}>
            {#each ($entries || []) as entry, index (JSON.stringify(entry.content))}
                <div draggable="true"
                     role="listitem"
                     on:dragstart={() => handleDragStart(index)}
                     on:dragenter={() => handleDragEnter(index)}
                     class="py-2 flex flex-row space-x-2" transition:slide>
                    <div class="pointer-events-none cursor-default flex-grow">
                        <ReportComponent entry={entry} survey={survey}/>
                    </div>
                    <div class="flex flex-col">
                        <Button variant="ghost" size="icon" on:click={() => openEditorExisting(entry)}>
                            <Pencil class="h-4 w-4"/>
                        </Button>
                    </div>
                </div>
            {/each}
        </div>
        <div class="py-2 pe-12">
            <NewEntry {openEditor} {openChooseNewEntryDialog}/>
        </div>
        <DivideByButton openDivideByDialog={openDivideByDialog} clear={resetDivideBy} draft={$draft} survey={survey}/>
        <div class="py-2 pe-12 flex flex-row justify-end">
            <Button on:click={generate}>Сгенерировать отчёт</Button>
        </div>
    </div>
{/if}
