<script lang="ts">
    import { onMount } from "svelte";
    import { PUBLIC_BASE_URL } from "$env/static/public";

    onMount(() => {
        window.YaSendSuggestToken(`${PUBLIC_BASE_URL}/oauth`, {})
    })
</script>

<svelte:head>
    <script src="https://yastatic.net/s3/passport-sdk/autofill/v1/sdk-suggest-token-with-polyfills-latest.js"></script>
    <title>OAuth</title>
</svelte:head>