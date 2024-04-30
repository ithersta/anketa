import type { PageServerLoad } from "./$types";
import { error } from "@sveltejs/kit";
import type { Dashboard } from "./DashboardSurvey";

export const load: PageServerLoad = async ({ fetch }) => {
    let response = await fetch(`https://api/dashboard/survey`)
    if (response.ok) {
        return {
            dashboard: await response.json() as Dashboard,
        }
    }

    error(response.status)
}
