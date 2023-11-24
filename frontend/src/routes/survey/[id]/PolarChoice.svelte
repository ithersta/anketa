<script lang="ts">
    import * as Card from "$lib/components/ui/card";
    import * as ToggleGroup from "$lib/components/ui/toggle-group";
    import { PolarChoice } from "$lib/survey/polarchoice";
    import ValidationResult from "./ValidationResult.svelte";
    import UnknownHint from "./UnknownHint.svelte";

    export let entry: PolarChoice.Entry
    export let answer: PolarChoice.Answer
    export let forceError: boolean

    let options = new Array(entry.range * 2 + 1).fill(null).map((_, i) => i - entry.range)
    let stringAnswers: string[] = []

    $: hints = PolarChoice.validate(entry, answer)
</script>

<Card.Root>
    <Card.Header>
        <Card.Title>{entry.question}</Card.Title>
    </Card.Header>
    <Card.Content>
        <ToggleGroup.Root type="single">
            {#each options as option (option)}
                <ToggleGroup.Item value={option.toString()}>{(option > 0 ? "+" : "") + option}</ToggleGroup.Item>
            {/each}
        </ToggleGroup.Root>
    </Card.Content>
    <Card.Footer>
        <ValidationResult {hints} {forceError} let:hint>
            {#if (hint.type === "Required")}
                <span>Выберите вариант</span>
            {:else}
                <UnknownHint/>
            {/if}
        </ValidationResult>
    </Card.Footer>
</Card.Root>