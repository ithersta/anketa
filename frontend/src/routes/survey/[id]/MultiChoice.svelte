<script lang="ts">
    import { slide } from 'svelte/transition';
    import * as Card from "$lib/components/ui/card";
    import { Input } from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import { Checkbox, Radio } from "$lib/components/ui/checkbox";
    import { MultiChoice } from "$lib/survey/multichoice";
    import ValidationResult from "./ValidationResult.svelte";
    import UnknownHint from "./UnknownHint.svelte";
    import { persisted } from "svelte-persisted-store";
    import type { Writable } from "svelte/store";
    import * as devalue from "devalue";
    import type { SurveyAnswer } from "$lib/survey/entries";
    import { Check } from "lucide-svelte";

    export let entry: MultiChoice.Entry
    export let forceError: boolean
    export let post: (uuid: string, answer: SurveyAnswer) => void

    let selected: Writable<Set<number>> = persisted(`answer-${entry.id}-selected`, new Set(), { serializer: devalue })
    let other: Writable<string> = persisted(`answer-${entry.id}-other`, "")

    let isRadio = MultiChoice.isRadio(entry)
    $: nullifiedOther = ($other === "") ? null : $other
    $: { if (nullifiedOther !== null && isRadio) selected.update((s) => {
        s.clear()
        return s
    }) }
    $: nullifiedAnswer = ($selected.size === 0 && $other === "") ? undefined : { type: "MultiChoice", selected: Array.from($selected), other: nullifiedOther }
    $: hints = MultiChoice.validate(entry, nullifiedAnswer)
    $: { post(entry.id, nullifiedAnswer) }
</script>

<Card.Root>
    <Card.Header>
        <Card.Title>{entry.question}</Card.Title>
    </Card.Header>
    <Card.Content>
        <div class="flex-col space-y-2">
            {#each entry.options as option, i}
                {#if (isRadio)}
                    <div class="flex items-center space-x-2">
                        <Radio id="checkbox-{entry.id}-{i}" checked={$selected.has(i)} onCheckedChange={
                            (checked) => {
                                if (checked) other.set("")
                                selected.update((s) => {
                                    s.clear()
                                    if (checked) s.add(i)
                                    return s
                                })
                            }
                        }/>
                        <Label for="checkbox-{entry.id}-{i}" class="text-sm font-medium leading-none">{option}</Label>
                    </div>
                {:else}
                    <div class="flex items-center space-x-2">
                        <Checkbox id="checkbox-{entry.id}-{i}" checked={$selected.has(i)} onCheckedChange={
                            (checked) => {
                                selected.update((s) => {
                                    checked ? s.add(i) : s.delete(i)
                                    return s
                                })
                            }
                        }/>
                        <Label for="checkbox-{entry.id}-{i}" class="text-sm font-medium leading-none">{option}</Label>
                    </div>
                {/if}
            {/each}
            {#if (entry.isAcceptingOther)}
                <div class="flex items-center">
                    {#if (nullifiedOther !== null)}
                        <div transition:slide={{axis: 'x'}} class="pe-2">
                            {#if (isRadio)}
                                <Radio id="checkbox-{entry.id}-other"
                                        checked={true}
                                        onCheckedChange={(checked) => { if (!checked) other.set("") }}/>
                            {:else}
                                <Checkbox id="checkbox-{entry.id}-other"
                                        checked={true}
                                        onCheckedChange={(checked) => { if (!checked) other.set("") }}/>
                            {/if}
                        </div>
                    {/if}
                    <Input type="text" placeholder="Другое" class="max-w-xs" bind:value={$other}/>
                </div>
            {/if}
        </div>
    </Card.Content>
    <Card.Footer>
        <ValidationResult {hints} {forceError} let:hint>
            {#if (hint.type === "MinChoiceNotMatched")}
                <span>Выберите не менее {entry.minSelected}</span>
            {:else if (hint.type === "MaxChoiceNotMatched")}
                <span>Выберите не более {entry.maxSelected}</span>
            {:else if (hint.type === "OtherMaxLengthExceeded")}
                <span>Поле «другое»: {$other.length}/{entry.otherMaxLength}</span>
            {:else}
                <UnknownHint/>
            {/if}
        </ValidationResult>
    </Card.Footer>
</Card.Root>