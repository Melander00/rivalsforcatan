export function isNumber(number: number, min: number = Number.MIN_VALUE, max: number = Number.MAX_VALUE): number is number {
    if(isNaN(number)) return false;

    if(number < min || number > max) return false;

    return true;
}