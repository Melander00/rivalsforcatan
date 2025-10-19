package samuel.event.card;

import samuel.event.Event;
import samuel.event.EventID;

public class TradeShipsRaceEvent implements Event {

    private static final EventID id = new EventID("event", "trade_ships_race_event");

    @Override
    public EventID getId() {
        return id;
    }

}
