package samuel.game;

import samuel.eventmanager.EventBus;
import samuel.player.Player;

import java.util.List;

/**
 * Readonly game state manager.
 */
public interface GameContext {

    /**
     * Gets the player whose turn it is.
     * @return
     */
    Player getActivePlayer();

    /**
     * Sets the next player as active player.
     */
    void switchTurn();

    /**
     * Gets all players
     * @return
     */
    List<Player> getPlayers();

    /**
     * Adds another player
     * @param player
     */
    void addPlayer(Player player);

    /**
     * Returns the EventBus singleton
     * @return
     */
    EventBus getEventBus();


}
