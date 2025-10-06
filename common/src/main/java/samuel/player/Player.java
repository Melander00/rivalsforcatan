package samuel.player;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.stack.CardStack;
import samuel.effect.Effect;
import samuel.resource.ResourceBundle;

import java.util.List;

public interface Player {

    /**
     * Returns the user's principality.
     * @return
     */
    Board getPrincipality();

    /**
     * Gives a one-time effect to the user.
     * @param effect
     */
    void giveEffect(Effect effect);

    /**
     * Asks the user for an integer.
     * @return
     */
    int requestInt();

    /**
     * Asks the user for an integer in the range
     * @param min
     * @param max
     * @return
     */
    int requestInt(int min, int max);

    /**
     * Asks the user to select an amount of resources from the bundle.
     * @param bundle
     * @param amount
     * @return A ResourceBundle containing whichever resources the player picked.
     */
    ResourceBundle requestResource(ResourceBundle bundle, int amount);

    /**
     * Generic method to ask which card stack to choose.
     * Example: Player should replenish cards, which stack to choose from?
     * @param cardStacks
     * @return
     */
    CardStack<?> requestCardStack(List<CardStack<?>> cardStacks);

    /**
     * Asks the user which board position to choose.
     * @return
     */
    BoardPosition requestBoardPosition();
}
