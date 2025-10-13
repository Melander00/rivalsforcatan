package samuel.event.die;

import samuel.die.EventDieFace;
import samuel.event.CancelableEvent;
import samuel.event.Event;
import samuel.event.EventID;
import samuel.player.Player;

public class EventDieEvent implements Event {

    private final static EventID id = new EventID("dice", "event");

    private final Player roller;
    private EventDieFace rollResults;

    public EventDieEvent(Player roller, EventDieFace rollResult) {
        this.roller = roller;
        this.rollResults = rollResult;
    }

    public EventDieFace getRollResults() {
        return this.rollResults;
    }

    public void setRollResults(EventDieFace rollResults) {
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

        private final static EventID id = new EventID("dice", "event_post");

        private final EventDieFace rollResults;

        public Post(EventDieFace rollResults) {
            this.rollResults = rollResults;
        }

        public EventDieFace getRollResults() {
            return this.rollResults;
        }

        @Override
        public EventID getId() {
            return id;
        }
    }
}
