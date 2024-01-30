export function parseIntStrict(str: string): number {
    if (str.length === 0 || ![...str].every(c => c >= '0' && c <= '9')) return NaN
    return Number(str)
}