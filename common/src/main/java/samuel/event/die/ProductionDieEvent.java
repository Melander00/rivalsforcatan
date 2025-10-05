package samuel.event.die;

import samuel.event.CancelableEvent;
import samuel.event.Event;
import samuel.player.Player;

public class ProductionDieEvent implements CancelableEvent {

    private boolean isCanceled = false;
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
    public boolean isCanceled() {
        return isCanceled;
    }

    @Override
    public void cancel() {
        this.isCanceled = true;
    }




    public static class Post implements Event {
        private final int rollResults;

        public Post(int rollResults) {
            this.rollResults = rollResults;
        }

        public int getRollResults() {
            return this.rollResults;
        }
    }
}
