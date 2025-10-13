export enum MessageType {
    GENERIC = "GENERIC",
    DIRECT_MESSAGE = "DIRECT_MESSAGE",
    EVENT = "EVENT",

    RESPONSE = "RESPONSE",

    REQUEST_HAND = "REQUEST_HAND",
    REQUEST_BOARD = "REQUEST_BOARD",
    
    REQUEST_INT = "REQUEST_INT",
    REQUEST_BOOL = "REQUEST_BOOL",
    REQUEST_CARD = "REQUEST_CARD",
    REQUEST_RESOURCE = "REQUEST_RESOURCE",
    REQUEST_CARD_STACK = "REQUEST_CARD_STACK",
    REQUEST_BOARD_POSITION = "REQUEST_BOARD_POSITION",

    REQUEST_ACTION = "REQUEST_ACTION"
}

export type Message = {
    type: MessageType,
    requestId: string,
    data: any
}

export enum Action {
    ROLL_DICE = "ROLL_DICE",
    PLAY_CARD = "PLAY_CARD",
    TRADE = "TRADE",
    END_TURN = "END_TURN",
}

export type ActionMessage = {
    action: Action,
    data: any
}

export type ActionResponse = {
    success: boolean,
    code: string
}