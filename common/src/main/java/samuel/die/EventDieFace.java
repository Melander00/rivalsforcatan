package samuel.die;

import samuel.card.CardID;
import samuel.game.GameContext;

public interface EventDieFace {

    void resolve(GameContext context);
    boolean hasPriorityOverProduction();

    CardID getId();

}
