<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    export let dialogOpen: boolean
</script>

<AlertDialog.Root bind:open={dialogOpen}>
    <AlertDialog.Content>
        <AlertDialog.Header>
            <AlertDialog.Title>Ответы отправлены!</AlertDialog.Title>
            <AlertDialog.Description>
                Ваши ответы были успешно отправлены и приняты к рассмотрению.
            </AlertDialog.Description>
        </AlertDialog.Header>
        <AlertDialog.Footer>
            <AlertDialog.Action>OK</AlertDialog.Action>
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>