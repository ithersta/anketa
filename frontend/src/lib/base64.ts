export async function bufferToBase64(buffer: ArrayBuffer): Promise<string> {
    const base64url: string = await new Promise(r => {
        const reader = new FileReader()
        reader.onload = () => r(reader.result as string)
        reader.readAsDataURL(new Blob([buffer]))
    });
    return base64url.slice(base64url.indexOf(',') + 1);
}