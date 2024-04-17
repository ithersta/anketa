<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    export let dialogOpen: boolean
    export let deleteDraft: () => {}
</script>

<AlertDialog.Root bind:open={dialogOpen}>
    <AlertDialog.Content>
        <AlertDialog.Header>
            <AlertDialog.Title>Удалить черновик отчёта?</AlertDialog.Title>
            <AlertDialog.Description>
                Это действие нельзя отменить.
            </AlertDialog.Description>
        </AlertDialog.Header>
        <AlertDialog.Footer>
            <AlertDialog.Cancel>Отменить</AlertDialog.Cancel>
            <AlertDialog.Action variant="destructive" on:click={deleteDraft}>Удалить</AlertDialog.Action>
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>
