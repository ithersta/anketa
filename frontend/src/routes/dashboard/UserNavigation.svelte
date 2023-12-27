<script lang="ts">
    import * as DropdownMenu from "$lib/components/ui/dropdown-menu";
    import * as Avatar from "$lib/components/ui/avatar";
    import { Button } from "$lib/components/ui/button";

    export let profile: {
        displayName: string,
        email: string,
    }

    async function logOut() {
        const response = await fetch("/oauth/logout", { method: "POST" })
        if (response.ok) {
            location.assign("/oauth")
        }
    }
</script>

<DropdownMenu.Root>
    <DropdownMenu.Trigger asChild let:builder>
        <Button
                variant="ghost"
                builders={[builder]}
                class="relative h-8 w-8 rounded-full"
        >
            <Avatar.Root class="h-8 w-8">
                <Avatar.Fallback>{profile.displayName.substring(0, 1)}</Avatar.Fallback>
            </Avatar.Root>
        </Button>
    </DropdownMenu.Trigger>
    <DropdownMenu.Content class="w-56" align="end">
        <DropdownMenu.Label class="font-normal">
            <div class="flex flex-col space-y-1">
                <p class="text-sm font-medium leading-none">{profile.displayName}</p>
                <p class="text-xs leading-none text-muted-foreground">{profile.email}</p>
            </div>
        </DropdownMenu.Label>
        <DropdownMenu.Separator />
        <DropdownMenu.Item on:click={logOut}>Выйти</DropdownMenu.Item>
    </DropdownMenu.Content>
</DropdownMenu.Root>