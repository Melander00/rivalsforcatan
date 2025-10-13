package samuel.event.die;

import samuel.event.CancelableEvent;
import samuel.event.Event;
import samuel.event.EventID;
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

    public static class Post implements Event {
        private final static EventID id = new EventID("dice", "production_post");
        private final int rollResults;

        public Post(int rollResults) {
            this.rollResults = rollResults;
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
