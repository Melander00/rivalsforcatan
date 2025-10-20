package samuel.condition;

import samuel.game.GameContext;
import samuel.player.Player;

public interface VictoryCondition {

    /**
     * Checks if the player supplied has won the game (for example has enough victory points)
     * @param player
     * @return
     */
    boolean hasWon(Player player, GameContext context);

}
