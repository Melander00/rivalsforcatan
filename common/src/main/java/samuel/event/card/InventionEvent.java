package samuel.event.card;

import samuel.event.CancelableEvent;
import samuel.event.Event;

public class InventionEvent implements CancelableEvent {
    private boolean isCanceled = false;

    @Override
    public void cancel() {
        this.isCanceled = true;
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }

    public static class Post implements Event {

    }

}
