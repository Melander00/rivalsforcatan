package samuel.player;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.Card;
import samuel.card.PlaceableCard;
import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.effect.Effect;
import samuel.event.Event;
import samuel.eventmanager.Subscribe;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.action.PlayerAction;
import samuel.player.request.RequestCause;
import samuel.point.Point;
import samuel.point.PointBundle;
import samuel.resource.Resource;
import samuel.resource.ResourceBundle;
import samuel.util.Pair;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

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
     * Removes an effect.
     * @param effect
     */
    void removeEffect(Effect effect);

    /**
     * Asks the user for an integer.
     * @return
     */
    int requestInt(RequestCause cause);

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
     * Returns the amount of a specific point type that the player has.
     * @param point
     * @return
     * @param <T>
     */
    <T extends Point> int getPoints(Class<T> point);

    /**
     * Whether this player has the most points of a type among all players in the context.
     * Cannot be used alone for determining strength advantage since we don't check for minimum points.
     * We only compare to other players.
     * @param pointType
     * @param context
     * @return true if this player has the most points of all players.
     */
    boolean hasPointAdvantage(Class<? extends Point> pointType, GameContext context);

    /**
     * Returns all the points a player has on the board.
     * Not including Victory Points from advantages or other sources which aren't from the board.
     * @return
     */
    PointBundle getPoints();

    /**
     * Returns the player's hand.
     * @return
     */
    PlayerHand getHand();

    /**
     * Removes the card from the player's hand.
     * @param card
     */
    void removeCardFromHand(PlayableCard card);

    /**
     * Whether the player's hand is full.
     * @return
     */
    boolean isHandFull();

    /**
     * Returns the maximum allowed number of cards that the player may hold.
     * @return
     */
    int getMaxHandSize();

    /**
     * Adds the card to the hand.
     * @param card
     */
    void addCardToHand(PlayableCard card);

    /**
     * Returns the card with the same UUID as supplied.
     * @param uuid
     * @return
     */
    PlayableCard getCardInHandFromUuid(UUID uuid);

    /**
     * Places the card on the board and initializes it.
     * @param card
     * @param position
     * @param context
     */
    void placeCard(PlaceableCard card, BoardPosition position, GameContext context);

    /**
     * Removes the card from the board.
     * @param card
     * @param position
     * @param context
     */
    void removeCard(PlaceableCard card, BoardPosition position, GameContext context);

    /**
     * Plays the card
     * @param card
     * @param context
     */
    void playCard(PlayableCard card, GameContext context);

    /**
     * Gives the player the resources.
     * @param bundle
     */
    void giveResources(ResourceBundle bundle);

    /**
     * Whether the player has at least a certain amount of resources.
     * @param bundle
     * @return
     */
    boolean hasResources(ResourceBundle bundle);

    /**
     * Removes resources from the player.
     * @param bundle
     */
    void removeResources(ResourceBundle bundle);

    /**
     * Gets all the resources the player has on the board.
     * @return
     */
    ResourceBundle  getResources();

    /**
     * Gets the amount of a specific resource the player has on the board.
     * @param resource
     * @return
     * @param <T>
     */
    <T extends Resource> int getResources(Class<T> resource);
}
