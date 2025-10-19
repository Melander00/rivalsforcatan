package samuel.game;

import samuel.player.Player;

public interface DiceHandler {

    void rollAndResolveDice(Player player, GameContext context);

}
