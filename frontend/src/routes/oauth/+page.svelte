<script lang="ts">
    import LoginWithYandex from "./yandex/LoginWithYandex.svelte";
</script>

<svelte:head>
    <title>Вход</title>
</svelte:head>

<div class="container max-w-prose">
    <div class="flex flex-col items-center justify-center h-screen">
        <div class="flex flex-col text-center">
            <h1 class="text-2xl font-semibold tracking-tight">
                Требуется вход
            </h1>
            <p class="text-sm text-muted-foreground">
                Чтобы продолжить, нажмите кнопку ниже
            </p>
            <div class="w-96 h-[56px] p-4">
                <LoginWithYandex/>
            </div>
        </div>
    </div>
</div>