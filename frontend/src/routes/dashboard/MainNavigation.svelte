<script lang="ts">
    import { cn } from "$lib/utils";

    let className: string | undefined | null = undefined;
    export { className as class };
</script>

<nav class={cn("flex items-center space-x-4 lg:space-x-6", className)}>
    <a
            href="/dashboard"
            class="text-sm font-medium transition-colors hover:text-primary"
    >
        Анкеты
    </a>
</nav>