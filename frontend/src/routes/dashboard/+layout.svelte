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

<div class="flex-col flex">
    <div class="border-b">
        <div class="flex h-16 items-center px-4 container">
            <MainNavigation class="mx-6"/>
            <div class="ml-auto flex items-center space-x-4">
                <UserNavigation {profile}/>
            </div>
        </div>
    </div>
    <div class="flex-1 pt-6 container">
        <slot/>
    </div>
</div>