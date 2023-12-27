import type { PageServerLoad } from "./$types";
import { error } from "@sveltejs/kit";
import type { DashboardSurvey } from "./DashboardSurvey";

export const load: PageServerLoad = async ({ fetch }) => {
    let response = await fetch(`https://api/dashboard/survey`)
    if (response.ok) {
        return {
            surveys: await response.json() as DashboardSurvey[],
        }
    }

    error(response.status)
}