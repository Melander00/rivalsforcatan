package samuel;

public enum MessageType {
    GENERIC,
    DIRECT_MESSAGE,
    EVENT,
    // Response
    RESPONSE,

    // Client requests
    REQUEST_HAND,
    REQUEST_BOARD,
    REQUEST_STATE,
    REQUEST_POINTS,
    REQUEST_STACKS,
    REQUEST_OPPONENT,

    // Server requests
    REQUEST_INT,
    REQUEST_BOOL,
    REQUEST_CARD,
    REQUEST_RESOURCE,
    REQUEST_CARD_STACK,
    REQUEST_BOARD_POSITION,

    REQUEST_ACTION, // the main server->client game request

    ;


    @Override
    public String toString() {
        return super.toString();
    }
}
