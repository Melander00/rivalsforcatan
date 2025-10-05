package samuel.card.action;

import samuel.card.Card;
import samuel.game.GameContext;
import samuel.player.Player;

public interface ActionCard extends Card {
    void play(Player owner, GameContext context);
}
