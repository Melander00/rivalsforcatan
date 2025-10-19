package samuel.game;

import samuel.player.Player;

public interface ExchangeHandler {

    void handleExchange(Player player, GameContext context);

}
