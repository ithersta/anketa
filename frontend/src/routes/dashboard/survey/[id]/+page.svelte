<script lang="ts">
    import * as Table from "$lib/components/ui/table";
    import { ArrowRight, Clipboard, Download, Eye, Plus, TableIcon } from "lucide-svelte";
    import { Button } from "$lib/components/ui/button";
    import * as Card from "$lib/components/ui/card";
    import { db } from "$lib/db/db";
    import { goto } from "$app/navigation";
    import { page } from "$app/stores";
    import { liveQuery } from "dexie";
    import { fromSurveyEntry, type ReportEntry } from "$lib/report/entries";
    import type { ReportDraftEntry } from "$lib/report/draft";

    export let data
    const surveyId = $page.params.id
    const survey = data.survey

    const reports = liveQuery(async () => {
        return db.reportDrafts
            .where("surveyId")
            .equals(surveyId)
            .toArray()
    })

    async function gotoNewReport() {
        let id: number = await db.transaction("rw", [db.reportDrafts, db.reportDraftEntries], async () => {
            let id: number = await db.reportDrafts.add({
                title: "Новый отчёт",
                surveyId: surveyId,
            })
            await db.reportDraftEntries.bulkAdd(
                survey.entries
                    .map(e => fromSurveyEntry(e))
                    .filter(e => e !== undefined)
                    .map(e => e as ReportEntry)
                    .map((e, index) => {
                        return {
                            draftId: id,
                            order: index,
                            content: e,
                        } satisfies ReportDraftEntry
                    })
            )
            return id
        })
        await goto(`/dashboard/survey/${surveyId}/export/report/${id}`)
    }
</script>

<svelte:head>
    <title>{survey.title}</title>
</svelte:head>

<div class="flex items-center justify-between space-y-2 pb-6">
    <h2 class="text-3xl font-bold tracking-tight">{survey.title}</h2>
</div>

<div class="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
    <Card.Root>
        <Card.Header>
            <div class="flex flex-row center justify-between space-y-0 pb-2">
                <div class="flex flex-col space-y-1.5">
                    <Card.Title>Просмотр анкеты</Card.Title>
                    <Card.Description>Анкета открыта</Card.Description>
                </div>
                <Eye class="h-4 w-4 text-muted-foreground" />
            </div>
        </Card.Header>
        <Card.Content>
            <Button variant="outline" class="w-full" href="/survey/{data.id}" target="_blank">
                <ArrowRight class="mr-2 h-4 w-4"/>
                Перейти
            </Button>
        </Card.Content>
    </Card.Root>
    <Card.Root>
        <Card.Header>
            <div class="flex flex-row center justify-between space-y-0 pb-2">
                <div class="flex flex-col space-y-1.5">
                    <Card.Title>Экспорт</Card.Title>
                    <Card.Description>Экспортировать ответы</Card.Description>
                </div>
                <Download class="h-4 w-4 text-muted-foreground" />
            </div>
        </Card.Header>
        <Card.Content>
            <Button variant="outline" class="w-full" href="/dashboard/survey/{data.id}/export/xlsx">
                <TableIcon class="mr-2 h-4 w-4"/>
                Таблица XLSX
            </Button>
        </Card.Content>
    </Card.Root>
</div>

<div class="flex items-center justify-between space-y-2 pb-6 pt-8">
    <h2 class="text-xl font-bold tracking-tight">Отчёты</h2>
    <Button variant="outline" on:click={gotoNewReport}>
        <Plus class="mr-2 h-4 w-4" />
        Новый отчёт
    </Button>
</div>

<Table.Root>
    <Table.Header>
        <Table.Row>
            <Table.Head>Название</Table.Head>
        </Table.Row>
    </Table.Header>
    <Table.Body>
        {#each $reports || [] as report (report.id)}
            <Table.Row class="cursor-pointer" on:click={() => {goto(`/dashboard/survey/${surveyId}/export/report/${report.id}`)}}>
                <Table.Cell>{report.title}</Table.Cell>
            </Table.Row>
        {/each}
    </Table.Body>
</Table.Root>
