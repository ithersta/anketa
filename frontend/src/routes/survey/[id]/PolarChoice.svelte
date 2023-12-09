<script lang="ts">
    import * as Card from "$lib/components/ui/card";
    import * as ToggleGroup from "$lib/components/ui/toggle-group";
    import { PolarChoice } from "$lib/survey/polarchoice";
    import ValidationResult from "./ValidationResult.svelte";
    import UnknownHint from "./UnknownHint.svelte";
    import { persisted } from "svelte-persisted-store";
    import type { Writable } from "svelte/store";
    import * as devalue from "devalue";
    import type { SurveyAnswer } from "$lib/survey/entries";

    export let entry: PolarChoice.Entry
    export let forceError: boolean
    export let post: (uuid: string, answer: SurveyAnswer) => void

    let answer: Writable<string | undefined> = persisted(`answer-${entry.id}`, undefined, { serializer: devalue })
    let options = new Array(entry.range * 2 + 1).fill(null).map((_, i) => i - entry.range)

    $: nullifiedAnswer = ($answer === undefined) ? undefined : { type: "PolarChoice", selected: Number.parseInt($answer) }
    $: hints = PolarChoice.validate(entry, nullifiedAnswer)
    $: { post(entry.id, nullifiedAnswer) }
</script>

<Card.Root>
    <Card.Header>
        <Card.Title>{entry.question}</Card.Title>
    </Card.Header>
    <Card.Content>
        <ToggleGroup.Root bind:value={$answer} type="single">
            {#each options as option (option)}
                <ToggleGroup.Item class="w-12 h-12" value={option.toString()}>
                    <span class="text-lg">{(option > 0 ? "+" : "") + option}</span>
                </ToggleGroup.Item>
            {/each}
        </ToggleGroup.Root>
    </Card.Content>
    <Card.Footer>
        <ValidationResult {hints} {forceError}>
            <UnknownHint/>
        </ValidationResult>
    </Card.Footer>
</Card.Root>