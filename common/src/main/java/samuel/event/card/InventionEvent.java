package samuel.event.card;

import samuel.event.CancelableEvent;
import samuel.event.Event;
import samuel.event.EventID;

public class InventionEvent implements Event {

    private static final EventID id = new EventID("event", "invention_event");

    @Override
    public EventID getId() {
        return id;
    }
}
