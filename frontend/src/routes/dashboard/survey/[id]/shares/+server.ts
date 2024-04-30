import { type RequestHandler } from "@sveltejs/kit";

export const POST: RequestHandler = async ({ params, fetch, request }) => {
    return await fetch(`https://api/dashboard/survey/${params.id}/shares`, {
        body: request.body,
        method: "POST",
        headers: { "Content-Type": "text/plain" },
        duplex: "half",
    })
}
