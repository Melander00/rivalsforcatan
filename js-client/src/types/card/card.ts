export type Card = {
    cardID: CardID,
    uuid: string,

    [key: string]: any
}

export type CardID = {
    namespace: string,
    id: string,
}

export type CardStack = {
    cards: Card[],
    uuid: string,
    size: number
}