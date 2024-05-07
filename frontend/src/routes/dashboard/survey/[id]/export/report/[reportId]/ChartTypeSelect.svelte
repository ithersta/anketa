<script lang="ts">
    import * as Select from "$lib/components/ui/select";
    import { MultiChoiceReport } from "$lib/report/multichoice";
    import ChartType = MultiChoiceReport.ChartType;
    import { get, type Writable } from "svelte/store";

    export let chartType: Writable<ChartType>
    const items: {
        value: ChartType,
        label: string,
    }[] = [
        { value: undefined, label: "Автоматически" },
        { value: "Pie", label: "Круговая" },
        { value: "Bar", label: "Столбчатая" },
    ]
    let selected = items.find((it) => it.value === get(chartType)) ?? items[0]
    $: {
        chartType.set(selected.value)
    }
</script>

<span class="text-lg font-semibold leading-none tracking-tight">Диаграмма</span>
<Select.Root bind:selected={selected}>
    <Select.Trigger class="w-[180px]">
        <Select.Value placeholder="Тип диаграммы"/>
    </Select.Trigger>
    <Select.Content>
        {#each items as item (item.value)}
            <Select.Item value={item.value}>{item.label}</Select.Item>
        {/each}
    </Select.Content>
</Select.Root>
