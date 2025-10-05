package samuel.event;

public interface CancelableEvent extends Event {
    boolean isCanceled();
    void cancel();
}
