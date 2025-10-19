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
     * Emits that an event has happened.
     *
     * @param event
     */
    @Subscribe
    void onEvent(Event event);

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

    PointBundle getPoints();

    PlayerHand getHand();
    void removeCardFromHand(PlayableCard card);
    boolean isHandFull();
    int getMaxHandSize();
    void addCardToHand(PlayableCard card);

    PlayableCard getCardInHandFromUuid(UUID uuid);

    void placeCard(PlaceableCard card, BoardPosition position, GameContext context);

    void removeCard(PlaceableCard card, BoardPosition position, GameContext context);

    void playCard(PlayableCard card, GameContext context);

    void giveResources(ResourceBundle bundle);
    boolean hasResources(ResourceBundle bundle);
    void removeResources(ResourceBundle bundle);

    ResourceBundle  getResources();
    <T extends Resource> int getResources(Class<T> resource);
}
