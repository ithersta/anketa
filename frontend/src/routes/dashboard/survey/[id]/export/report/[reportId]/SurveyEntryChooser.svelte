<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import { getShortName, type SurveyEntry } from "$lib/survey/entries";
    import { ScrollArea } from "$lib/components/ui/scroll-area";
    export let dialogOpen: boolean
    export let entries: SurveyEntry[]
    export let onSelect: (SurveyEntry) => void
</script>

<AlertDialog.Root bind:open={dialogOpen}>
    <AlertDialog.Content>
        <AlertDialog.Header>
            <AlertDialog.Title>Выбор вопроса</AlertDialog.Title>
        </AlertDialog.Header>
        <ScrollArea class="h-80 rounded-md border">
            <div class="p-4">
                {#each entries as entry(entry.id)}
                    <button
                            on:click={() => onSelect(entry)}
                            class="flex items-center gap-3 rounded-lg px-3 py-2 text-muted-foreground transition-all hover:text-primary"
                    >
                    {getShortName(entry)}
                    </button>
                {/each}
            </div>
        </ScrollArea>
        <AlertDialog.Footer>
            <AlertDialog.Cancel>Отменить</AlertDialog.Cancel>
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>
