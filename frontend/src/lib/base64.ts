export async function bufferToBase64(buffer: ArrayBuffer) {
    const base64url = await new Promise(r => {
        const reader = new FileReader()
        reader.onload = () => r(reader.result)
        reader.readAsDataURL(new Blob([buffer]))
    });
    return base64url.slice(base64url.indexOf(',') + 1);
}