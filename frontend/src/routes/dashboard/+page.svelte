<script lang="ts">
    import * as Table from "$lib/components/ui/table";
    import { Button } from "$lib/components/ui/button";
    import { goto } from "$app/navigation";
    import { Plus } from "lucide-svelte";
    import { db } from "$lib/db/db";
    import { liveQuery } from "dexie";

    export let data
    let surveys = data.surveys
    const drafts = liveQuery(async () => {
        return db.surveyDrafts
            .orderBy("id")
            .toArray();
    })

    async function gotoBuilder() {
        let id: number = await db.surveyDrafts.add({ title: "Новая анкета" })
        await goto(`/dashboard/builder/${id}`)
    }
</script>

<svelte:head>
    <title>Дашборд</title>
</svelte:head>

<div class="flex items-center justify-between space-y-2 pb-6">
    <h2 class="text-3xl font-bold tracking-tight">Анкеты</h2>
    <Button variant="outline" on:click={gotoBuilder}>
        <Plus class="mr-2 h-4 w-4" />
        Новая анкета
    </Button>
</div>

<Table.Root>
    <Table.Header>
        <Table.Row>
            <Table.Head>Название</Table.Head>
            <Table.Head class="w-64">Создана</Table.Head>
        </Table.Row>
    </Table.Header>
    <Table.Body>
        {#each $drafts || [] as draft (draft.id)}
            <Table.Row class="cursor-pointer" on:click={() => {goto(`/dashboard/builder/${draft.id}`)}}>
                <Table.Cell>{draft.title}</Table.Cell>
                <Table.Cell>Черновик</Table.Cell>
            </Table.Row>
        {/each}
        {#each surveys as survey (survey.id)}
            <Table.Row class="cursor-pointer" on:click={() => {goto(`/dashboard/survey/${survey.id}`)}}>
                <Table.Cell>{survey.title}</Table.Cell>
                <Table.Cell>{(new Date(survey.createdAt)).toLocaleString("ru-RU")}</Table.Cell>
            </Table.Row>
        {/each}
    </Table.Body>
</Table.Root>