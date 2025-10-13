package samuel.die;

import samuel.game.GameContext;

public interface EventDieFace {

    void resolve(GameContext context);
    boolean hasPriorityOverProduction();

}
