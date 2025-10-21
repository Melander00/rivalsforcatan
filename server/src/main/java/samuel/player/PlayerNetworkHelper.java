package samuel.player;

import samuel.Message;
import samuel.board.BoardPosition;
import samuel.card.Card;
import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.phase.Phase;
import samuel.player.action.PlayerAction;
import samuel.player.request.RequestCause;
import samuel.resource.ResourceBundle;
import samuel.util.Pair;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public interface PlayerNetworkHelper {

    /**
     * Asks the user for an integer in the range
     * @param min
     * @param max
     * @return
     */
    int requestInt(int min, int max, RequestCause cause);

    /**
     * Asks the user to select an amount of resources from the bundle.
     * @param bundle
     * @param amount
     * @return A <code>ResourceBundle</code> containing whichever resource the player picked.
     */
    ResourceBundle requestResource(ResourceBundle bundle, int amount, RequestCause cause);

    /**
     * Generic method to ask which card stack to choose.
     * Example: Player should replenish cards, which stack to choose from?
     *
     * @param cardStacks
     * @return
     */
    CardStack<PlayableCard> requestCardStack(List<CardStack<PlayableCard>> cardStacks, List<UUID> unselectableStackIds, RequestCause cause);

    /**
     * Asks the user which board position to choose from the 2d list of positions. It is up to the caller to determine validity.
     * @return
     */
    BoardPosition requestBoardPosition(List<List<BoardPosition>> positions, RequestCause cause);

    /**
     * Asks the user which card to choose.
     * @return
     */
    <T extends Card> T requestCard(List<T> cards, RequestCause cause);

    /**
     * Asks the user a yes/no question.
     * @return
     */
    boolean requestBoolean(RequestCause cause);

    /**
     * Main loop action request
     * @param phase
     * @return
     */
    Pair<PlayerAction, BiConsumer<Boolean,String>> requestAction(Phase phase);

    /**
     * Send a message to the player from the server
     * @param msg
     */
    void directMessage(String msg);

    /**
     * Sends a message to the user connected to the player.
     * @param message
     */
    void sendMessage(Message message);

    /**
     * Adds a listener for Message
     * @param listener
     */
    void addListener(BiConsumer<Message, Player> listener, Player owner);
}
