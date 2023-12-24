<script lang="ts">
    import { ArrowRightFromLine, Download, Eye, Table } from "lucide-svelte";
    import { Button } from "$lib/components/ui/button";
    import * as Card from "$lib/components/ui/card";
    import { goto } from "$app/navigation";

    export let data
    let survey = data.survey

    async function exportXlsx() {
        goto(`/dashboard/survey/${data.id}/export/xlsx`)
    }
</script>

<div class="flex items-center justify-between space-y-2 pb-6">
    <h2 class="text-3xl font-bold tracking-tight">{survey.title}</h2>
</div>

<div class="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
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
            <Button variant="outline" class="w-full" on:click={exportXlsx}>
                <Table class="mr-2 h-4 w-4"/>
                Таблица XLSX
            </Button>
        </Card.Content>
    </Card.Root>
</div>
