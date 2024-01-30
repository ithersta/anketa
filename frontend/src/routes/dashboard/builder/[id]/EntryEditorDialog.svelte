<script lang="ts">
    import * as Dialog from "$lib/components/ui/dialog";
    import type { EntryEditorDialogState } from "./EntryEditorDialogState";
    import { Button } from "$lib/components/ui/button";
    import EntryEditor from "./EntryEditor.svelte";
    import { get, type Readable } from "svelte/store";
    import type { ValidationHint } from "$lib/survey/validation";
    import type { SurveyEntry } from "$lib/survey/entries";
    import { db } from "$lib/db/db";

    export let close: () => void
    export let surveyId: number
    export let state: EntryEditorDialogState
    let forceError = false

    async function save() {
        if (!state) return
        const hints = get(state.uiState.hints as Readable<ValidationHint[]>)
        if (hints.some((hint) => hint.isError)) {
            forceError = true
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
</script>

<Dialog.Root open={state !== undefined} closeOnEscape={false} closeOnOutsideClick={false}>
    <Dialog.Content>
        <Dialog.Header>
            <Dialog.Title>Новый блок</Dialog.Title>
        </Dialog.Header>
        <EntryEditor uiState={state.uiState} {forceError}/>
        <Dialog.Footer>
            <Button on:click={save}>
                {#if state.id}Сохранить{:else}Добавить{/if}
            </Button>
        </Dialog.Footer>
    </Dialog.Content>
</Dialog.Root>
