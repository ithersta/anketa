<script lang="ts">
    import * as Dialog from "$lib/components/ui/dialog";
    import type { EntryEditorDialogState } from "./EntryEditorDialogState";
    import { Button } from "$lib/components/ui/button";
    import EntryEditor from "./EntryEditor.svelte";
    import { get, type Readable } from "svelte/store";
    import type { ValidationHint } from "$lib/survey/validation";
    import type { SurveyEntry } from "$lib/survey/entries";
    import { db } from "$lib/db/db";
    import { Trash } from "lucide-svelte";

    export let close: () => void
    export let surveyId: number
    export let state: EntryEditorDialogState
    let forceError = false

    async function save() {
        if (!state) return
        const hints = get(state.uiState.hints as Readable<ValidationHint[]>)
        if (hints.some((hint) => hint.isError)) {
            forceError = true
            return
        }
        const entry = get(state.uiState.entry as Readable<SurveyEntry>)
        if (!state.id) {
            await db.transaction("rw", [db.surveyDraftEntries], async () => {
                let maxOrder = -1
                await db.surveyDraftEntries
                    .where("surveyId")
                    .equals(surveyId)
                    .each((entry) => { maxOrder = Math.max(maxOrder, entry.order) })
                await db.surveyDraftEntries.add({
                    surveyId: surveyId,
                    order: maxOrder + 1,
                    content: entry,
                })
            })
        } else {
            await db.surveyDraftEntries.update(state.id, {
                content: entry,
            })
        }
        forceError = false
        close()
    }

    async function deleteEntry(id: number) {
        await db.surveyDraftEntries.delete(id)
        forceError = false
        close()
    }
</script>

<Dialog.Root open={state !== undefined} onOpenChange={(isOpen) => {
    forceError = false
    if (!isOpen) {
        state = undefined
    }
}}>
    <Dialog.Content class="max-w-xl">
        <div class="flex flex-col space-y-6">
            <EntryEditor uiState={state.uiState} {forceError}/>
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
