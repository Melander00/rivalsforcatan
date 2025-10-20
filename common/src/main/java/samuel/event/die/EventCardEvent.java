package samuel.event.die;

import samuel.event.ContextEvent;
import samuel.event.EventID;
import samuel.game.GameContext;

public class EventCardEvent implements ContextEvent {

    private static final EventID id = new EventID("dice_event", "event_card");
    private final GameContext context;

    public EventCardEvent(GameContext context) {
        this.context = context;
    }

    @Override
    public GameContext getContext() {
        return context;
    }

    @Override
    public EventID getId() {
        return id;
    }
}
