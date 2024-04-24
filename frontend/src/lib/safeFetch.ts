import { toast } from "svelte-sonner";

export async function safeFetch(url: string, options = {}): Promise<Response | undefined> {
    try {
        const response = await fetch(url, options)
        if (response.ok) {
            return response
        }
        toast.error(await response.text())
    } catch (e: any) {
        toast.error(e.message)
    }
    return undefined
}
