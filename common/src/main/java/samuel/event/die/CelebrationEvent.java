package samuel.event.die;

import samuel.event.ContextEvent;
import samuel.event.EventID;
import samuel.game.GameContext;

public class CelebrationEvent implements ContextEvent {

    private static final EventID id = new EventID("dice_event", "celebration");
    private final GameContext context;

    public CelebrationEvent(GameContext context) {
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
