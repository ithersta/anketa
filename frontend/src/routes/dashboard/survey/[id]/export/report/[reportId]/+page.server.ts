import type { PageServerLoad } from "./$types";
import { error } from "@sveltejs/kit";
import type { SurveyContent } from "$lib/survey/survey";

export const load: PageServerLoad = async ({ params, fetch }) => {
    let response = await fetch(`https://api/survey/${params.id}`)
    if (response.ok) {
        return {
            id: params.id,
            survey: await response.json() as SurveyContent,
        }
    }

    error(response.status)
}
