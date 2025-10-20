package samuel.deck;

import samuel.card.Card;
import samuel.card.PlayableCard;
import samuel.card.event.EventCard;
import samuel.card.region.RegionCard;

import java.util.List;

public interface Deck {


    /**
     * Returns all the cards to be placed in the region card stack.
     * @return
     */
    List<RegionCard> getRegionCards();

    /**
     * Returns all the cards to be placed in the region card stack.
     * @return
     */
    List<EventCard> getEventCards();

    /**
     * Returns all the cards to be placed in basic card stacks.
     * @return
     */
    List<PlayableCard> getBasicCards();

    /**
     * Returns all the theme cards
     * @return empty list if not theme game.
     */
    List<PlayableCard> getThemeCards();

    /**
     * For game-expansions
     * @return empty list if not theme game.
     */
    List<Card> getFaceUpThemeCards();

    /**
     * Returns the amount of Road cards for the center stack.
     * @return
     */
    int getAmountOfRoadCards();

    /**
     * Returns the amount of Settlement cards for the center stack.
     * @return
     */
    int getAmountOfSettlementCards();

    /**
     * Returns the amount of City cards for the center stack.
     * @return
     */
    int getAmountOfCityCards();
}
