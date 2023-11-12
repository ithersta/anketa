import type { HandleFetch } from "@sveltejs/kit";

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
    if (request.url.startsWith("https://api/")) {
        request = new Request(
            request.url.replace("https://api/", "http://localhost:8080/"),
            request
        )
        let token = event.cookies.get("Authorization")
        if (token) {
            request.headers.set("Authorization", `Bearer ${token}`)
        }
    }
    return fetch(request)
}