<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import { safeFetch } from "$lib/safeFetch";
    import { page } from "$app/stores";
    import { Label } from "$lib/components/ui/label";
    import { Input } from "$lib/components/ui/input";
    import { Button } from "$lib/components/ui/button";
    import { toast } from "svelte-sonner";

    export let dialogOpen: boolean

    const surveyId = $page.params.id
    let email = ""
    let requestCount = 0

    async function onDone() {
        if (requestCount > 0) return
        requestCount++
        const response = await safeFetch(`/dashboard/survey/${surveyId}/shares`, {
            method: "POST",
            body: email,
        })
        if (response && response.ok) {
            dialogOpen = false
            email = ""
            toast.success("Указанному пользователю открыт доступ к анкете")
        }
        requestCount--
    }
</script>

<AlertDialog.Root bind:open={dialogOpen}>
    <AlertDialog.Content>
        <AlertDialog.Header>
            <AlertDialog.Title>Поделиться доступом</AlertDialog.Title>
            <AlertDialog.Description>
                Пользователь получит полный доступ к управлению анкетой.
            </AlertDialog.Description>
        </AlertDialog.Header>
        <div class="grid w-full gap-2">
            <Label for="email">Email</Label>
            <Input bind:value={email} id="email"></Input>
        </div>
        <AlertDialog.Footer>
            <AlertDialog.Cancel>Отменить</AlertDialog.Cancel>
            <Button on:click={onDone} disabled={requestCount !== 0}>Готово</Button>
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>
