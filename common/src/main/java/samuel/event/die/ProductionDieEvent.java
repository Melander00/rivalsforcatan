package samuel.event.die;

import samuel.event.CancelableEvent;
import samuel.event.ContextEvent;
import samuel.event.Event;
import samuel.event.EventID;
import samuel.game.GameContext;
import samuel.player.Player;

public class ProductionDieEvent implements Event {

    private final static EventID id = new EventID("dice", "production");

    private final Player roller;
    private int rollResults;

    public ProductionDieEvent(Player roller, int rollResult) {
        this.roller = roller;
        this.rollResults = rollResult;
    }

    public int getRollResults() {
        return this.rollResults;
    }

    public void setRollResults(int rollResults) {
        this.rollResults = rollResults;
    }

    public Player getRoller() {
        return this.roller;
    }

    @Override
    public EventID getId() {
        return id;
    }

    public static class Post implements ContextEvent {
        private final static EventID id = new EventID("dice", "production_post");
        private final int rollResults;
        private final GameContext context;

        public Post(int rollResults, GameContext context) {
            this.rollResults = rollResults;
            this.context = context;
        }

        @Override
        public GameContext getContext() {
            return context;
        }

        public int getRollResults() {
            return this.rollResults;
        }

        @Override
        public EventID getId() {
            return id;
        }
    }
}
