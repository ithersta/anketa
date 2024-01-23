<script lang="ts">
    import { mode } from 'mode-watcher';
    import { onMount } from "svelte";
    import { PUBLIC_BASE_URL, PUBLIC_YANDEX_OAUTH_CLIENT_ID } from "$env/static/public";
    import { page } from "$app/stores";

    onMount(() => {
        window.YaAuthSuggest.init({
                client_id: PUBLIC_YANDEX_OAUTH_CLIENT_ID,
                response_type: 'token',
                redirect_uri: `${PUBLIC_BASE_URL}/oauth/yandex`
            },
            PUBLIC_BASE_URL, {
                view: 'button',
                parentId: 'yandex-button',
                buttonView: 'main',
                buttonTheme: $mode,
                buttonSize: 'xxl',
                buttonBorderRadius: 16
            }
        )
            .then(function (result: any) {
                return result.handler()
            })
            .then(async function (data: { access_token: string }) {
                let response = await fetch("oauth/yandex", {
                    method: "POST",
                    body: JSON.stringify({ token: data.access_token }),
                    headers: { "Content-Type": "application/json" },
                })
                if (response.ok) {
                    location.assign($page.url.searchParams.get("next") ?? "/")
                }
            })
            .catch(function (error: any) {
                console.log(error)
            })
    })
</script>

<svelte:head>
    <script src="https://yastatic.net/s3/passport-sdk/autofill/v1/sdk-suggest-with-polyfills-latest.js"></script>
</svelte:head>

<div id="yandex-button"/>