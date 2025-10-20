package samuel.die;

import samuel.card.CardID;
import samuel.game.GameContext;

public interface EventDieFace {

    /**
     * Resolves the event associated with the roll.
     * @param context
     */
    void resolve(GameContext context);

    /**
     * Whether the event should be resolved before production dice. (Brigand Attack)
     * @return
     */
    boolean hasPriorityOverProduction();

    /**
     * Unique ID
     * @return
     */
    CardID getId();

}
