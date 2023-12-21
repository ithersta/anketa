<script lang="ts">
    import type { Text } from "$lib/survey/text";

    export let uiState: Text.UiState
</script>

<span>{uiState.entry.content}</span>