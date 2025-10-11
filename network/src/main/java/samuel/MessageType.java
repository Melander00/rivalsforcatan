package samuel;

public enum MessageType {
    GENERIC,
    DIRECT_MESSAGE,

    // Client requests
    REQUEST_HAND,
    REQUEST_BOARD,

    // Server requests
    REQUEST_INT,
    REQUEST_RESOURCE,
    REQUEST_CARD_STACK,
    REQUEST_BOARD_POSITION,

    // Response
    RESPONSE,
    ;


    @Override
    public String toString() {
        return super.toString();
    }
}
