import type { PageServerLoad } from "./$types";
import { error } from "@sveltejs/kit";

export const load: PageServerLoad = async ({ fetch }) => {
    let response = await fetch(`https://api/profile`)
    if (response.ok) {
        return {
            profile: await response.json(),
        }
    }

    error(response.status)
}