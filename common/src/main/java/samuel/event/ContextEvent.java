package samuel.event;

import samuel.game.GameContext;

/**
 * An event that has the current game context in it.
 */
public interface ContextEvent extends Event {

    /**
     * Returns the game context for which this event was fired in.
     * @return
     */
    GameContext getContext();
}
