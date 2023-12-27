import { redirect, type RequestHandler } from "@sveltejs/kit";

export const POST: RequestHandler = async ({ cookies }) => {
    cookies.delete("Authorization", {
        httpOnly: true,
        path: "/",
        secure: true,
    })
    return new Response()
}