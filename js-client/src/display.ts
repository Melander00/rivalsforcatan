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
function printPrincipality(board: GridBoard): string {
    const sb: string[] = [];
    const positions = board.boardPositions;

    if (positions.length === 0) return "Empty board";

    // Determine board size
    const rows = Math.max(...positions.map(p => p.row)) + 1;
    const cols = Math.max(...positions.map(p => p.column)) + 1;

    // Build a 2D array for easier indexing
    const grid: GridPosition[][] = Array.from({ length: rows }, () => Array.from({ length: cols }, () => ({
        uuid: "",
        card: { cardID: { namespace: "", id: "" } },
        row: 0,
        column: 0,
        empty: true
    })));

    positions.forEach(pos => {
        grid[pos.row][pos.column] = pos;
    });

    // Compute column widths
    const w: number[] = new Array(cols).fill(0);
    const minW = 10;
    for (let c = 0; c < cols; c++) {
        let m = minW;
        for (let r = 0; r < rows; r++) {
            const pos = grid[r][c];
            m = Math.max(m, cellTitle(pos).length, cellInfo(pos).length);
        }
        w[c] = m;
    }

    // Top column headers
    sb.push("      " + Array.from({ length: cols }, (_, c) => padRight(`Col ${c}`, w[c] + 3)).join("") + "\n");

    // Top border
    sb.push("    " + buildSep(w) + "\n");

    // Each row
    for (let r = 0; r < rows; r++) {
        // Title line
        sb.push(`${r.toString().padStart(2, " ")} |`);
        for (let c = 0; c < cols; c++) {
            sb.push(` ${padRight(cellTitle(grid[r][c]), w[c])} |`);
        }
        sb.push("\n");

        // Info line
        sb.push("   |");
        for (let c = 0; c < cols; c++) {
            sb.push(` ${padRight(cellInfo(grid[r][c]), w[c])} |`);
        }
        sb.push("\n");

        // Row separator
        sb.push("    " + buildSep(w) + "\n");
    }

    // Points line
    return sb.join("");
}