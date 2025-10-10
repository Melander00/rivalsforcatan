import { Card } from "../card/card"

export type GridPosition = {
    uuid: string,
    card: Card,
    empty: boolean, 
}

export type GridBoard = {
    boardPositions: GridPosition[][]
}