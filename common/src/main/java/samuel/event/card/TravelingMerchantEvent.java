package samuel.event.card;

import samuel.event.Event;
import samuel.event.EventID;

public class TravelingMerchantEvent implements Event {

    private static final EventID id = new EventID("event", "traveling_merchant_event");

    @Override
    public EventID getId() {
        return id;
    }

}
