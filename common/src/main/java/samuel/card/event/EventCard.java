package samuel.card.event;

import samuel.card.Card;
import samuel.game.GameContext;

public interface EventCard extends Card {
    void resolveEvent(GameContext context);
}
