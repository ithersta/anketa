export type ValidationHint = {
    type: string,
    isHint: boolean,
    isError: boolean,
}

export function requiredHint(entry: { isRequired: boolean }, answer: any | undefined): {
    type: "Required",
    isHint: boolean,
    isError: boolean,
} {
    return {
        type: "Required",
        isHint: entry.isRequired,
        isError: entry.isRequired && answer === undefined
    }
}