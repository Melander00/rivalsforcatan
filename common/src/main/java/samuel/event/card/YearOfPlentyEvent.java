package samuel.event.card;

import samuel.event.Event;
import samuel.event.EventID;

public class YearOfPlentyEvent implements Event {

    private static final EventID id = new EventID("event", "year_of_plenty_event");

    @Override
    public EventID getId() {
        return id;
    }

}
