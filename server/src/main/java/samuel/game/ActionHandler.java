package samuel.game;

import samuel.player.Player;

import java.util.function.BiConsumer;

public interface ActionHandler {

    void handleActionPhase(Player player, GameContext context);

    void handlePlayCardAction(Player player, GameContext context, Object data, BiConsumer<Boolean, String> callback);

    void handleTradeAction(Player player, GameContext context, Object data, BiConsumer<Boolean, String> callback);

    void handleBuildAction(Player player, GameContext context, Object data, BiConsumer<Boolean, String> callback);

}
