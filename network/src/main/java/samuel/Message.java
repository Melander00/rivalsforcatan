package samuel;

import java.util.UUID;

public class Message {
    private MessageType type;
    private UUID requestId;
    private Object data;

    public Message() {}

    public Message(MessageType type, Object data) {
        this.type = type;
        this.requestId = UUID.randomUUID();
        this.data = data;
    }

    public Message(MessageType type, UUID requestId, Object data) {
        this.type = type;
        this.requestId = requestId;
        this.data = data;
    }

    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }

    public UUID getRequestId() {return requestId;}

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
