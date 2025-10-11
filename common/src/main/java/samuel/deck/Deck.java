package samuel.deck;

import samuel.card.Card;
import samuel.card.event.EventCard;
import samuel.card.region.RegionCard;

import java.util.List;

public interface Deck {


    /**
     * Does NOT return the cards used in principality
     * @return
     */
    List<RegionCard> getRegionCards();

    List<EventCard> getEventCards();

    List<Card> getBasicCards();

    /**
     * For game-expansions
     * @return null if there is no theme
     */
    List<Card> getThemeCards();

    /**
     * For game-expansions
     * @return null if there is no theme
     */
    List<Card> getFaceUpThemeCards();

    /**
     * Does NOT include starting principality cards.
     * Instead of returning a <code>List < RoadCard ></code> (which increases coupling) we let the game context handle how to create the road cards.
     * @return
     */
    int getAmountOfRoadCards();

    /**
     * Does NOT include starting principality cards
     * @return
     */
    int getAmountOfSettlementCards();

    int getAmountOfCityCards();
}
