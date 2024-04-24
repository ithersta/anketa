<script lang="ts">
    import * as Card from "$lib/components/ui/card";
    import * as ToggleGroup from "$lib/components/ui/toggle-group";
    import { PolarChoice } from "$lib/survey/polarchoice";
    import ValidationResult from "./ValidationResult.svelte";
    import UnknownHint from "./UnknownHint.svelte";

    export let uiState: PolarChoice.UiState
    export let forceError: boolean

    let { entry, selected, hints } = uiState
    let options = new Array(entry.range * 2 + 1).fill(null).map((_, i) => i - entry.range)
</script>

<Card.Root>
    <Card.Header>
        <Card.Title>{entry.question}</Card.Title>
    </Card.Header>
    <Card.Content>
        {#if entry.minText || entry.maxText}
            <div class="flex flex-row justify-between text-muted-foreground text-sm">
                <span>{entry.minText}</span>
                <span>{entry.maxText}</span>
            </div>
        {/if}
        <ToggleGroup.Root bind:value={$selected} type="single">
            {#each options as option (option)}
                <ToggleGroup.Item class="w-12 h-12" value={option.toString()}>
                    <span class="text-lg">{(option > 0 ? "+" : "") + option}</span>
                </ToggleGroup.Item>
            {/each}
        </ToggleGroup.Root>
    </Card.Content>
    <Card.Footer>
        <ValidationResult hints={$hints} {forceError}>
            <UnknownHint/>
        </ValidationResult>
    </Card.Footer>
</Card.Root>
