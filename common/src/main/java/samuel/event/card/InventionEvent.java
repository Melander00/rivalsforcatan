package samuel.event.card;

import samuel.event.CancelableEvent;
import samuel.event.Event;
import samuel.event.EventID;

public class InventionEvent implements CancelableEvent {
    private boolean isCanceled = false;

    private static final EventID id = new EventID("event", "invention_event");


    @Override
    public void cancel() {
        this.isCanceled = true;
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }

    @Override
    public EventID getId() {
        return id;
    }

    public static class Post implements Event {
        private static final EventID id = new EventID("event", "invention_event_post");
        @Override
        public EventID getId() {
            return id;
        }
    }

}
