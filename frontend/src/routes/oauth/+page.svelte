<script>
    import {onMount} from "svelte";

    onMount(() => {
            window.onload = function () {
                window.YaAuthSuggest.init({
                        client_id: '4ab33cd575a54a2aa3617fddabff8388',
                        response_type: 'token',
                        redirect_uri: 'https://localhost/oauth/yandex'
                    },
                    'https://localhost/oauth', {
                        view: 'button',
                        parentId: 'container',
                        buttonView: 'main',
                        buttonTheme: 'light',
                        buttonSize: 'm',
                        buttonBorderRadius: 16
                    }
                )
                    .then(function (result) {
                        return result.handler()
                    })
                    .then(async function (data) {
                        let response = await fetch("oauth/yandex", {
                            method: "POST",
                            body: JSON.stringify({token: data.access_token})
                        })
                        if (response.status === 200) {
                            window.location.href = "/"
                        }
                    })
                    .catch(function (error) {
                        console.log(error)
                    })
            }
        }
    )
</script>

<svelte:head>
    <script src="https://yastatic.net/s3/passport-sdk/autofill/v1/sdk-suggest-with-polyfills-latest.js"></script>
</svelte:head>