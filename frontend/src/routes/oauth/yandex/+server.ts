import type { RequestHandler } from "@sveltejs/kit";

export const POST: RequestHandler = async ({ cookies, request, fetch }) => {
    let yandexToken = (await request.json()).token
    let response = await fetch("https://api/oauth/yandex", {
        body: JSON.stringify({ token: yandexToken }),
        method: "POST",
        headers: { "Content-Type": "application/json" }
    })
    let token = (await response.json()).token
    if (token) {
        cookies.set("Authorization", token, {
            httpOnly: true,
            path: "/",
            secure: true,
        })
    }
    return new Response()
}