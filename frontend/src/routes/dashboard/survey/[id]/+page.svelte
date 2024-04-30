<script lang="ts">
    import * as Table from "$lib/components/ui/table";
    import { ArrowRight, Clipboard, CopyIcon, Download, Eye, Plus, ShareIcon, TableIcon } from "lucide-svelte";
    import { Button } from "$lib/components/ui/button";
    import * as Card from "$lib/components/ui/card";
    import { db } from "$lib/db/db";
    import { goto } from "$app/navigation";
    import { page } from "$app/stores";
    import { liveQuery } from "dexie";
    import { fromSurveyEntry, type ReportEntry } from "$lib/report/entries";
    import type { ReportDraftEntry } from "$lib/report/draft";
    import type { SurveyDraftEntry } from "$lib/builder/draft";
    import ShareDialog from "./ShareDialog.svelte";

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

    async function createCopy() {
        let id = await db.transaction("rw", [db.surveyDrafts, db.surveyDraftEntries], async () => {
            let id = await db.surveyDrafts.add({ title: `${survey.title} – Копия` })
            let draftEntries = survey.entries.map((e, index) => {
                return {
                    surveyId: id,
                    order: index,
                    content: e,
                } satisfies SurveyDraftEntry
            })
            await db.surveyDraftEntries.bulkAdd(draftEntries)
            return id
        })
        await goto(`/dashboard/builder/${id}`)
    }

    let isShareDialogOpen = false

    function openShareDialog() {
        isShareDialogOpen = true
    }
</script>

<svelte:head>
    <title>{survey.title}</title>
</svelte:head>

<ShareDialog bind:dialogOpen={isShareDialogOpen}/>

<div class="flex items-center justify-between space-y-2 pb-6">
    <h2 class="text-3xl font-bold tracking-tight">{survey.title}</h2>
</div>

<div class="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
    <Card.Root>
        <Card.Header>
            <div class="flex flex-row center justify-between space-y-0 pb-2">
                <div class="flex flex-col space-y-1.5">
                    <Card.Title>Управление анкетой</Card.Title>
                    <Card.Description>Анкета открыта</Card.Description>
                </div>
                <Eye class="h-4 w-4 text-muted-foreground" />
            </div>
        </Card.Header>
        <Card.Content>
            <div class="flex flex-col gap-2">
                <Button variant="outline" class="w-full" href="/survey/{data.id}" target="_blank">
                    <ArrowRight class="mr-2 h-4 w-4"/>
                    Перейти к анкете
                </Button>
                <Button variant="outline" class="w-full" on:click={createCopy}>
                    <CopyIcon class="mr-2 h-4 w-4"/>
                    Создать копию анкеты
                </Button>
                <Button variant="outline" class="w-full" on:click={openShareDialog}>
                    <ShareIcon class="mr-2 h-4 w-4"/>
                    Поделиться доступом
                </Button>
            </div>
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
            <div class="flex flex-col gap-2">
                <Button variant="outline" class="w-full" href="/dashboard/survey/{data.id}/export/xlsx">
                    <TableIcon class="mr-2 h-4 w-4"/>
                    Таблица XLSX
                </Button>
                <Button variant="outline" class="w-full" href="/dashboard/survey/{data.id}/export/csv">
                    <TableIcon class="mr-2 h-4 w-4"/>
                    Таблица CSV
                </Button>
            </div>
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
