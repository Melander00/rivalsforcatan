package samuel.event.card;

import samuel.event.Event;
import samuel.event.EventID;

public class YuleEvent implements Event {

    private static final EventID id = new EventID("event", "invention_event");

    @Override
    public EventID getId() {
        return id;
    }


}
