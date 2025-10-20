package samuel.event.die;

import samuel.event.ContextEvent;
import samuel.event.EventID;
import samuel.game.GameContext;

public class TradeEvent implements ContextEvent {

    private static final EventID id = new EventID("dice_event", "trade");
    private final GameContext context;

    public TradeEvent(GameContext context) {
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
