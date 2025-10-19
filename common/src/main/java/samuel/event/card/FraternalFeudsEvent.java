package samuel.event.card;

import samuel.event.Event;
import samuel.event.EventID;

public class FraternalFeudsEvent implements Event {

    private static final EventID id = new EventID("event", "fraternal_feuds_event");

    @Override
    public EventID getId() {
        return id;
    }

}
