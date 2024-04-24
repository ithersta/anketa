import { toast } from "svelte-sonner";

export async function safeFetch(url: string, options = {}) {
    try {
        const response = await fetch(url, options)
        if (!response.ok) {
            toast(await response.text())
        } else {
            return response
        }
    } catch (e: any) {
        toast(e.message)
    }
}
