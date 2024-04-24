import type { RequestHandler } from "@sveltejs/kit";

export const POST: RequestHandler = async ({ params, request, fetch }) => {
    return await fetch(`https://api/answer/${params.id}`, {
        body: request.body,
        method: "POST",
        headers: { "Content-Type": "application/json" },
        duplex: "half",
    })
}
