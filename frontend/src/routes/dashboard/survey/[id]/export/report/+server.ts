import { type RequestHandler } from "@sveltejs/kit";

export const POST: RequestHandler = async ({ params, request, fetch }) => {
    return fetch(`https://api/dashboard/survey/${params.id}/export/report`, {
        body: request.body,
        method: "POST",
        headers: { "Content-Type": "application/json" },
        duplex: "half",
    })
}
