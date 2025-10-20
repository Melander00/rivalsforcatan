package samuel.event;

import samuel.game.GameContext;

/**
 * An event that classes may subscribe to.
 */
public interface Event {
    /**
     * An ID identifying which type of event was fired.
     * @return
     */
    EventID getId();
}
