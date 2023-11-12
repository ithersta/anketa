<script lang="ts">
    import { onMount } from "svelte";
    import { PUBLIC_BASE_URL } from "$env/static/public";

    onMount(() => {
        window.onload = function () {
            window.YaSendSuggestToken(PUBLIC_BASE_URL, {})
        }
    })
</script>

<svelte:head>
    <script src="https://yastatic.net/s3/passport-sdk/autofill/v1/sdk-suggest-token-with-polyfills-latest.js"></script>
</svelte:head>