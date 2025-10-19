package samuel.event;

import samuel.game.GameContext;

public interface ContextEvent extends Event {
    GameContext getContext();
}
