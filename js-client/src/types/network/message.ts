export enum MessageType {
    GENERIC = "GENERIC",
    DIRECT_MESSAGE = "DIRECT_MESSAGE",
    
    REQUEST_BOARD = "REQUEST_BOARD",
    
}

export type Message = {
    type: MessageType,
    requestId: string,
    data: any
}