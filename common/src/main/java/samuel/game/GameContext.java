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

    /**
     * Returns the phase of the game.
     * @return
     */
    Phase getPhase();

    /**
     * Sets the phase of the game.
     * @param phase
     */
    void setPhase(Phase phase);

    /**
     * Rolls the event dice and returns the result.
     * @return
     */
    EventDieFace rollEventDice();

    /**
     * Rolls the production dice and returns the result.
     * @return
     */
    Integer rollProductionDie();

    /**
     * Returns which player has the strength advantage.
     * @return null if no player has it.
     */
    Player getStrengthAdvantage();

    /**
     * Returns which player has the trade advantage.
     * @return null if no player has it.
     */
    Player getTradeAdvantage();

    /**
     * Returns the amount of victory points for the supplied player.
     * @param player
     * @return
     */
    int getVictoryPoints(Player player);
}
