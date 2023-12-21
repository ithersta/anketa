<script lang="ts">
    import { slide } from 'svelte/transition';
    import * as Card from "$lib/components/ui/card";
    import { Input } from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import { Checkbox, Radio } from "$lib/components/ui/checkbox";
    import { MultiChoice } from "$lib/survey/multichoice";
    import ValidationResult from "./ValidationResult.svelte";
    import UnknownHint from "./UnknownHint.svelte";
    import { Check } from "lucide-svelte";

    export let uiState: MultiChoice.UiState
    export let forceError: boolean

    let { entry, selected, other, hints } = uiState
    let isRadio = MultiChoice.isRadio(entry)
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
                    {#if ($other.length > 0)}
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
        <ValidationResult hints={$hints} {forceError} let:hint>
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