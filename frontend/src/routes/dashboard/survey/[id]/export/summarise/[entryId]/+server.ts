import { error, type RequestHandler } from "@sveltejs/kit";

export const POST: RequestHandler = async ({ params, fetch }) => {
    const response = await fetch(`https://api/dashboard/survey/${params.id}/export/summarise/${params.entryId}`, {
        method: "POST",
    })
    if (response.ok) {
        return response
    }
    error(response.status)
}
