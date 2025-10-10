export enum MessageType {
    GENERIC = "GENERIC",
    REQUEST_BOARD = "REQUEST_BOARD"
}

export type Message = {
    type: MessageType,
    requestId: string,
    data: any
}