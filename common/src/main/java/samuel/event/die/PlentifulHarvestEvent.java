package samuel.event.die;

import samuel.event.Event;
import samuel.event.EventID;

public class PlentifulHarvestEvent implements Event {

    private static final EventID id = new EventID("dice_event", "plentiful_harvest");

    @Override
    public EventID getId() {
        return id;
    }
}
