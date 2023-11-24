<script lang="ts">
    import type { Text } from "$lib/survey/text";

    export let entry: Text.Entry
</script>

<span>{entry.content}</span>