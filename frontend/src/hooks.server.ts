import type { HandleFetch } from "@sveltejs/kit";
import { API_BASE_URL } from "$env/static/private";

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
    if (request.url.startsWith("https://api/")) {
        request = new Request(
            request.url.replace("https://api/", `${API_BASE_URL}/`),
            request
        )
        let token = event.cookies.get("Authorization")
        if (token) {
            request.headers.set("Authorization", `Bearer ${token}`)
        }
    }
    return fetch(request)
}