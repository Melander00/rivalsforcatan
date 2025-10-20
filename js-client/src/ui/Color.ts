
export function AnsiCode(code: string): (text: string) => string
export function AnsiCode(code: string[]): (text: string) => string

export function AnsiCode(code: string | string[]): (text: string) => string {
    if(Array.isArray(code)) {
        return (text: string) => {
            return `\x1b[${code.join(";")}m${text}\x1B[0m`
        }
    }

    return (text: string) => {
        return `\x1b[${code}m${text}\x1B[0m`
    }
}

export const Color = {
    red: AnsiCode(["38;5","196"]),
    green: AnsiCode(["38;5","46"]),
    orange: AnsiCode(["38;5","202"]),

} as const