<script lang="ts">
    import * as Dialog from "$lib/components/ui/dialog";
    import type { EntryEditorDialogState } from "./EntryEditorDialogState";
    import { Button } from "$lib/components/ui/button";
    import EntryEditor from "./EntryEditor.svelte";
    import { get, type Readable } from "svelte/store";
    import type { ReportEntry } from "$lib/report/entries";
    import { db } from "$lib/db/db";
    import { Trash } from "lucide-svelte";
    import type { ValidationHint } from "$lib/survey/validation";
    import type { SurveyContent } from "$lib/survey/survey";

    export let close: () => void
    export let draftId: number
    export let state: EntryEditorDialogState
    export let survey: SurveyContent
    let forceError = false

    async function save() {
        if (!state) return
        const hints = get(state.uiState.hints as Readable<ValidationHint[]>)
        if (hints.some((hint) => hint.isError)) {
            forceError = true
            return
        }
        const entry = get(state.uiState.entry as Readable<ReportEntry>)
        if (!state.id) {
            await db.transaction("rw", [db.reportDraftEntries], async () => {
                let maxOrder = -1
                await db.reportDraftEntries
                    .where("draftId")
                    .equals(draftId)
                    .each((entry) => { maxOrder = Math.max(maxOrder, entry.order) })
                await db.reportDraftEntries.add({
                    draftId: draftId,
                    order: maxOrder + 1,
                    content: entry,
                })
            })
        } else {
            await db.reportDraftEntries.update(state.id, {
                content: entry,
            })
        }
        forceError = false
        close()
    }

    async function deleteEntry(id: number) {
        await db.reportDraftEntries.delete(id)
        forceError = false
        close()
    }
</script>

<Dialog.Root preventScroll={false} open={state !== undefined} onOpenChange={(isOpen) => {
    forceError = false
    if (!isOpen) {
        state = undefined
    }
}}>
    <Dialog.Content class="max-w-xl">
        <div class="flex flex-col space-y-6">
            <EntryEditor uiState={state.uiState} {forceError} {survey}/>
        </div>
        <Dialog.Footer>
            {#if state.id}
                <Button variant="ghost" size="icon" on:click={() => deleteEntry(state.id)}>
                    <Trash class="h-4 w-4"/>
                </Button>
                <Button on:click={save}>
                    Сохранить
                </Button>
            {:else}
                <Button on:click={save}>
                    Добавить
                </Button>
            {/if}
        </Dialog.Footer>
    </Dialog.Content>
</Dialog.Root>
