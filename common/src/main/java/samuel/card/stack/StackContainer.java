package samuel.card.stack;

import samuel.card.Card;
import samuel.card.PlaceableCard;
import samuel.card.PlayableCard;
import samuel.card.center.CityCard;
import samuel.card.event.EventCard;
import samuel.card.region.RegionCard;

import java.util.List;

public interface StackContainer {

    /**
     * Returns the card stack handling the road cards.
     * @return
     */
    CardStack<PlaceableCard> getRoadStack();

    /**
     * Returns the card stack handling the settlement cards.
     * @return
     */
    CardStack<PlaceableCard> getSettlementStack();

    /**
     * Returns the card stack handling the region cards.
     * @return
     */
    CardStack<RegionCard> getRegionStack();

    /**
     * Returns the card stack handling the city cards.
     * @return
     */
    CardStack<PlaceableCard> getCityStack();

    /**
     * Returns the card stack handling the event cards.
     * @return
     */
    CardStack<EventCard> getEventStack();

    /**
     * Returns a specific stack by its index.
     * @return
     */
    CardStack<PlayableCard> getBasicStack(int stackIndex);

    /**
     * Adds a stack to the basic stacks.
     * @param stack
     */
    void addToBasicStacks(CardStack<PlayableCard> stack);

    /**
     * Returns a specific stack by its index.
     * @return
     */
    CardStack<PlayableCard> getThemeStack(int stackIndex);

    /**
     * Adds a stack to the theme stacks.
     * @param stack
     */
    void addToThemeStacks(CardStack<PlayableCard> stack);

    /**
     * Returns all the basic card stacks.
     * @return
     */
    List<CardStack<PlayableCard>> getBasicStacks();

    /**
     * Returns all the theme card stacks.
     * @return
     */
    List<CardStack<PlayableCard>> getThemeStacks();

//    CardStack<Card> getDiscardPile();
//    void discardCard(Card card);
}
