package samuel.game;

import samuel.die.EventDieFace;
import samuel.player.Player;

public interface DiceHandler {

    void rollAndResolveDice(Player player, GameContext context);
    int rollProductionDice(Player activePlayer, GameContext context);
    EventDieFace rollEventDice(Player activePlayer, GameContext context);
}
