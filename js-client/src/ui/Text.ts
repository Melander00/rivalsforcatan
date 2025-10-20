import util from "node:util";

export function CalculateWidth(text: string, skipFormatting = true): number {
    const str = skipFormatting ? util.stripVTControlCharacters(text) : text;
    return str.length;
}