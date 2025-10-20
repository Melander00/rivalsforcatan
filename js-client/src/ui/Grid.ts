import { GetCardInfo } from "../resources/ResourceHandler";
import { GridPosition } from "../types/board/board";
import { handleTemplate } from "./Template";
import { CalculateWidth } from "./Text";

export function buildPrincipality(positions: GridPosition[][]) {
    const sb: string[] = [];
    const principality = positions;

    const rows = principality.length;
    const cols = rows > 0 ? principality[0].length : 0;

    if (rows === 0 || cols === 0) return "Empty board";

    // Compute column widths
    const minW = 10;
    const widths = Array.from({ length: cols }, (_, c) => {
        let maxW = minW;
        for (let r = 0; r < rows; r++) {
            const pos = principality[r][c];
            maxW = Math.max(maxW, CalculateWidth(getCellTitle(pos)), CalculateWidth(getCellInfo(pos)));
        }
        return maxW;
    });

    // Column headers
    sb.push("      ");
    for (let c = 0; c < cols; c++) {
        sb.push(padRight(`Col ${c}`, widths[c] + 3));
    }
    sb.push("\n");

    // Top border
    sb.push("    " + buildSep(widths) + "\n");

    // Rows
    for (let r = 0; r < rows; r++) {
        // Title line
        sb.push(`${r.toString().padStart(2, " ")} |`);
        for (let c = 0; c < cols; c++) {
            const pos = principality[r][c];
            sb.push(` ${padRight(getCellTitle(pos), widths[c])} |`);
        }
        sb.push("\n");

        // Info line
        sb.push("   |");
        for (let c = 0; c < cols; c++) {
            const pos = principality[r][c];
            sb.push(` ${padRight(getCellInfo(pos), widths[c])} |`);
        }
        sb.push("\n");

        // Row separator
        sb.push("    " + buildSep(widths) + "\n");
    }


    return sb.join("");

}




function padRight(str: string, length: number): string {
    return str + " ".repeat(Math.max(0, length - CalculateWidth(str)));
}

function buildSep(widths: number[]): string {
    return widths.map(w => "-".repeat(w + 2)).join("+");
}




function getCellTitle(pos: GridPosition): string {
    if(pos.empty) return ""

    const cardId = pos.card.cardID;
    const cardInfo = GetCardInfo(cardId)
    if(!cardInfo) return `${cardId.namespace}:${cardId.id}`
    if(!cardInfo.board?.name) return `${cardInfo.name}`

    return handleTemplate(pos.card, cardInfo.board?.name, `${cardId.namespace}:${cardId.id}`)
}

function getCellInfo(pos: GridPosition): string {
    if(pos.empty) return ""

    const cardId = pos.card.cardID;
    const cardInfo = GetCardInfo(cardId)
    if(!cardInfo || !cardInfo.board?.description) return ""

    return handleTemplate(pos.card, cardInfo.board.description, `${cardId.namespace}:${cardId.id}`)
}






