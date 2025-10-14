import { GridBoard } from "./board/board"
import { Point } from "./point/point"

export type Opponent = {
    points: Point[],
    board: GridBoard,
}