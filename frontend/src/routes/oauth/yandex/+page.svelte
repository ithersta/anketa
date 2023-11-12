<script>
    import {onMount} from "svelte";

    onMount(() => {
            window.onload = function () {
                window.YaSendSuggestToken("https://localhost/oauth", {})
            }
        }
    )
</script>

<svelte:head>
    <script src="https://yastatic.net/s3/passport-sdk/autofill/v1/sdk-suggest-token-with-polyfills-latest.js"></script>
</svelte:head>