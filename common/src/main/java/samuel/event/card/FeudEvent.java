package samuel.event.card;

import samuel.event.Event;
import samuel.event.EventID;

public class FeudEvent implements Event {

    private static final EventID id = new EventID("event", "feud_event");

    @Override
    public EventID getId() {
        return id;
    }

}
