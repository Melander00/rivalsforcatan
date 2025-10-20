package samuel.card.event;

import samuel.card.Card;
import samuel.game.GameContext;

public interface EventCard extends Card {

    /**
     * Resolve what happens in the event. Is run AFTER the card is put in the stack again.
     * @param context
     */
    void resolveEvent(GameContext context);
}
