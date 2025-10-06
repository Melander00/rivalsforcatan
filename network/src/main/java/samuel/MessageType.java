package samuel;

public enum MessageType {

    // Client requests
    REQUEST_HAND,
    REQUEST_BOARD,

    // Server requests
    REQUEST_INT,
    REQUEST_RESOURCE,
    REQUEST_BOARD_POSITION
    ;



    @Override
    public String toString() {
        return super.toString();
    }
}
