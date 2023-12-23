<script lang="ts">
    import MainNavigation from "./MainNavigation.svelte";
    import UserNavigation from "./UserNavigation.svelte";

    export let data: {
        profile: {
            displayName: string,
            email: string,
        },
    }
    let profile = data.profile
</script>

<div class="hidden flex-col md:flex">
    <div class="border-b">
        <div class="flex h-16 items-center px-4">
            <MainNavigation class="mx-6"/>
            <div class="ml-auto flex items-center space-x-4">
                <UserNavigation {profile}/>
            </div>
        </div>
    </div>
    <div class="flex-1 space-y-4 p-8 pt-6">
        <slot/>
    </div>
</div>