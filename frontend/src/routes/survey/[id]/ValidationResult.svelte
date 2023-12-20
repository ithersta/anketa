<script lang="ts" generics="T extends ValidationHint">
    import { slide } from 'svelte/transition';
    import type { ValidationHint } from "$lib/survey/validation";

    export let hints: T[]
    export let forceError: boolean

    let wasValid = false
    $: isValid = hints.every((hint) => hint.isError === false)
    $: displayError = wasValid || forceError
    $: if (isValid) wasValid = true
</script>

<div class="flex-col w-full">
    {#each hints.filter((hint) => hint.isError && displayError || hint.isHint) as hint (hint.type)}
        <span
                transition:slide
                class:text-destructive={hint.isError && displayError}
                class:text-primary={isValid}
                class='flex justify-end items-center h-8'>
            {#if hint.isError && displayError}
                <svg transition:slide class="h-8 w-8 pr-2 fill-destructive" viewBox="0 0 24 24" opacity={(hint.isError && displayError) ? 1 : 0}>
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/>
                </svg>
            {/if}
            {#if hint.type === "Required"}
                <span>Обязательный вопрос</span>
            {:else}
                <slot {hint}/>
            {/if}
        </span>
    {/each}
</div>