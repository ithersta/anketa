import { error, type RequestHandler } from "@sveltejs/kit";

export const GET: RequestHandler = async ({ params, fetch }) => {
    const response = await fetch(`https://api/dashboard/survey/${params.id}/export/xlsx`)
    if (response.ok) {
        return response
    }
    error(response.status)
}