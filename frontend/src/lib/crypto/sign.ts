import { getSurveyAnswerKeys } from "$lib/crypto/generate";
import { bufferToBase64 } from "$lib/base64";

const ALGORITHM = { name: "ECDSA", hash: { name: "SHA-256" } }

export async function signSurveyAnswers(message: string) {
    let keys = await getSurveyAnswerKeys()
    let encoder = new TextEncoder()
    let encoded = encoder.encode(message)
    let signed = await crypto.subtle.sign(ALGORITHM, keys.privateKey, encoded)
    return {
        publicKey: keys.publicKey,
        content: message,
        signed: await bufferToBase64(signed),
    }
}