package samuel.game;

import samuel.card.Card;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.die.EventDieFace;
import samuel.eventmanager.EventBus;
import samuel.phase.Phase;
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

    /**
     * Checks whether the supplied player has won the game.
     * @param player
     * @return
     */
    boolean hasWon(Player player);

    /**
     * Gets the stack container
     */
    StackContainer getStackContainer();

    Phase getPhase();

    void setPhase(Phase phase);

    EventDieFace rollEventDice();
    Integer rollProductionDie();

}
