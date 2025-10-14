package samuel.player.request;

public record RequestCause(RequestCauseEnum type, Object data) {

    public RequestCause(RequestCauseEnum cause) {
        this(cause, null);
    }

}
