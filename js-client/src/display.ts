import { getCardInfo } from "./resourceHandler";
import { GridBoard, GridPosition } from "./types/board/board";




export function displayBoard(grid: GridBoard) {
    console.log(printPrincipality(grid))
}

// Helper functions
function cellTitle(pos: GridPosition): string {
    return pos.empty ? "" : getCardInfo(pos.card.cardID).name;
}

function cellInfo(pos: GridPosition): string {
    return pos.empty ? "" : pos.card.cardID.namespace;
}

function padRight(str: string, length: number): string {
    return str + " ".repeat(Math.max(0, length - str.length));
}

function buildSep(widths: number[]): string {
    return widths.map(w => "-".repeat(w + 2)).join("+");
}

// Main function
export function printPrincipality(board: GridBoard): string {
    const sb: string[] = [];
    const principality = board.boardPositions;

    const rows = principality.length;
    const cols = rows > 0 ? principality[0].length : 0;

    if (rows === 0 || cols === 0) return "Empty board";

    // Compute column widths
    const minW = 10;
    const widths = Array.from({ length: cols }, (_, c) => {
        let maxW = minW;
        for (let r = 0; r < rows; r++) {
            const pos = principality[r][c];
            maxW = Math.max(maxW, cellTitle(pos).length, cellInfo(pos).length);
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
        sb.push(`${r.toString().padStart(2, " ")}  |`);
        for (let c = 0; c < cols; c++) {
            const pos = principality[r][c];
            sb.push(` ${padRight(cellTitle(pos), widths[c])} |`);
        }
        sb.push("\n");

        // Info line
        sb.push("    |");
        for (let c = 0; c < cols; c++) {
            const pos = principality[r][c];
            sb.push(` ${padRight(cellInfo(pos), widths[c])} |`);
        }
        sb.push("\n");

        // Row separator
        sb.push("    " + buildSep(widths) + "\n");
    }


    return sb.join("");
}