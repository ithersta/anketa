import type { PageServerLoad } from "./$types";
import { error } from "@sveltejs/kit";

export const load: PageServerLoad = async ({ params, fetch }) => {
    let response = await fetch(`https://api/survey/${params.id}`)
    if (response.ok) {
        return {
            id: params.id,
            survey: await response.json(),
        }
    }

    throw error(response.status)
}