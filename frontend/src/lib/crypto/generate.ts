const PRIVATE_KEY = "survey_answer_private_key"
const PUBLIC_KEY = "survey_answer_public_key"
const ALGORITHM = { name: "ECDSA", namedCurve: "P-256" }

export async function getSurveyAnswerKeys() {
    const localStorage = window.localStorage
    const jwk = localStorage.getItem(PRIVATE_KEY)
    const publicKey = localStorage.getItem(PUBLIC_KEY)
    if (jwk === null || publicKey === null) {
        const keys = await generateSurveyAnswerKeys()
        const exportedPrivateKey = await crypto.subtle.exportKey("jwk", keys.privateKey)
        localStorage.setItem(PRIVATE_KEY, JSON.stringify(exportedPrivateKey))
        const exportedPublicKey = await crypto.subtle.exportKey("raw", keys.publicKey)
        const base64String = btoa(String.fromCharCode(...new Uint8Array(exportedPublicKey)))
        localStorage.setItem(PUBLIC_KEY, base64String)
        return { privateKey: keys.privateKey, publicKey: base64String }
    } else {
        let privateKey = await crypto.subtle.importKey(
            "jwk",
            JSON.parse(jwk),
            ALGORITHM,
            true,
            ["sign"]
        )
        return { privateKey: privateKey, publicKey: publicKey }
    }
}

async function generateSurveyAnswerKeys() {
    return await crypto.subtle.generateKey(
        ALGORITHM,
        true,
        ["sign"],
    )
}