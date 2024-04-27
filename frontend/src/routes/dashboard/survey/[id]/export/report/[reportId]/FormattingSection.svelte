<script lang="ts">
    import { ScrollArea } from "$lib/components/ui/scroll-area";
    import * as Tooltip from "$lib/components/ui/tooltip";

    export let formattingProperties: string
</script>

<div class="flex flex-row items-baseline">
    <span class="text-lg font-semibold leading-none tracking-tight">Форматирование</span>
    <Tooltip.Root openDelay={0} class="flex-grow">
        <Tooltip.Trigger>
            <p class="text-sm text-muted-foreground text-end pl-4">Пример шаблона</p>
        </Tooltip.Trigger>
        <Tooltip.Content>
            Варианты ответа $&lbrace;t1&rbrace; и $&lbrace;t2&rbrace; суммарно выбрали $&lbrace;с1 + с2&rbrace; чел.
        </Tooltip.Content>
    </Tooltip.Root>
</div>
<ScrollArea class="h-40 rounded-md border">
    <div class="p-4">
        <span class="format-hint text-muted-foreground text-sm">
            {formattingProperties}
            ac – общее количество ответов
            nac – количество воздержавшихся
        </span>
    </div>
</ScrollArea>

<style>
    .format-hint {
        white-space: pre-line;
    }
</style>
