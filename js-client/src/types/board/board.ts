import { Card } from "../card/card"

export type GridPosition = {
    uuid: string,
    card: Card,
    row: number,
    column: number,
    empty: boolean, 
}

export type GridBoard = {
    boardPositions: GridPosition[]
}