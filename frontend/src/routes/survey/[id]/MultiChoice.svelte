<script lang="ts">
    import * as Card from "$lib/components/ui/card";
    import { MultiChoice } from "$lib/survey/multichoice";
    import ValidationResult from "./ValidationResult.svelte";
    import UnknownHint from "./UnknownHint.svelte";

    export let entry: MultiChoice.Entry
    export let answer: MultiChoice.Answer = {
        selected: [],
        other: undefined,
    }

    export let forceError: boolean

    let radioAnswer: number | undefined = undefined
    let checkboxAnswer: (number | undefined)[] = []
    let isRadio = MultiChoice.isRadio(entry)
    $: nullifiedAnswer = (answer.selected.length === 0 && answer.other === undefined) ? undefined : answer
    $: hints = MultiChoice.validate(entry, nullifiedAnswer)
    $: {
        answer.selected = [radioAnswer].filter(it => it !== undefined) as number[]
    }
    $: {
        answer.selected = checkboxAnswer.filter(it => it !== undefined) as number[]
    }
</script>

<Card.Root>
    <Card.Header>
        <Card.Title>{entry.question}</Card.Title>
    </Card.Header>
    <Card.Content>
    {#each entry.options as option, i}
        <label class="flex items-center space-x-2">
            {#if (isRadio)}
                <input type="radio"
                       name="radio-{entry.id}"
                       class="radio"
                       value={i}
                       bind:group={radioAnswer}/>
            {:else}
                <input type="checkbox"
                       name="checkbox-{entry.id}"
                       class="checkbox"
                       value={i}
                       bind:group={checkboxAnswer}/>
            {/if}
            <span>{option}</span>
        </label>
    {/each}
    </Card.Content>
    <Card.Footer>
        <ValidationResult {hints} {forceError} let:hint>
            {#if (hint.type === "MinChoiceNotMatched")}
                <span>Выберите не менее {entry.minSelected}</span>
            {:else if (hint.type === "MaxChoiceNotMatched")}
                <span>Выберите не более {entry.maxSelected}</span>
            {:else}
                <UnknownHint/>
            {/if}
        </ValidationResult>
    </Card.Footer>
</Card.Root>