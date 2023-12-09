<script lang="ts">
	import { Checkbox as CheckboxPrimitive } from "bits-ui";
	import { Circle } from "lucide-svelte";
	import { cn } from "$lib/utils";

	type $$Props = CheckboxPrimitive.Props;
	type $$Events = CheckboxPrimitive.Events;

	let className: $$Props["class"] = undefined;
	export let checked: $$Props["checked"] = false;
	export { className as class };
</script>

<CheckboxPrimitive.Root
	class={cn(
		"box-content peer h-5 w-5 shrink-0 rounded-full border border-primary ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 data-[state=checked]:bg-primary data-[state=checked]:text-primary-foreground data-[disabled=true]:cursor-not-allowed data-[disabled=true]:opacity-50",
		className
	)}
	bind:checked
	{...$$restProps}
	on:click
>
	<CheckboxPrimitive.Indicator
		class={cn("flex items-center justify-center text-current h-5 w-5")}
		let:isChecked
	>
		{#if isChecked}
			<Circle class="h-3 w-3 fill-current text-current" />
		{/if}
	</CheckboxPrimitive.Indicator>
</CheckboxPrimitive.Root>
