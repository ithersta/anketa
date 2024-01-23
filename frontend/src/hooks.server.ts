import { type HandleFetch, redirect } from "@sveltejs/kit";
import { API_BASE_URL } from "$env/static/private";

const API_PREFIX = "https://api/"

export const handleFetch: HandleFetch = async ({ event, request, fetch }) => {
    if (request.url.startsWith(API_PREFIX)) {
        request = new Request(
            request.url.replace(API_PREFIX, `${API_BASE_URL}/`),
            request
        )
        let token = event.cookies.get("Authorization")
        if (token) {
            request.headers.set("Authorization", `Bearer ${token}`)
        }
    }
    let response = await fetch(request)
    if (response.status === 401) {
        redirect(302, "/oauth")
    }
    return response
}