<script lang="ts">
    import { Label } from "$lib/components/ui/label";
    import { Checkbox } from "$lib/components/ui/checkbox";

    export let isRequired: boolean
</script>

<div class="flex items-center space-x-2">
    <Checkbox id="required" bind:checked={isRequired}/>
    <Label for="required" class="text-sm font-medium leading-none">Обязательный вопрос</Label>
</div>