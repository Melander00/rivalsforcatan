package samuel.card.stack;

import samuel.card.Card;
import samuel.card.PlaceableCard;
import samuel.card.PlayableCard;
import samuel.card.center.CityCard;
import samuel.card.event.EventCard;
import samuel.card.region.RegionCard;

import java.util.List;

public interface StackContainer {

    CardStack<PlaceableCard> getRoadStack();

    CardStack<PlaceableCard> getSettlementStack();

    CardStack<RegionCard> getRegionStack();

    CardStack<PlaceableCard> getCityStack();

    CardStack<EventCard> getEventStack();

    CardStack<PlayableCard> getBasicStack(int stackIndex);

    void addToBasicStacks(CardStack<PlayableCard> stack);

    CardStack<PlayableCard> getThemeStack(int stackIndex);

    void addToThemeStacks(CardStack<PlayableCard> stack);

    List<CardStack<PlayableCard>> getBasicStacks();
    List<CardStack<PlayableCard>> getThemeStacks();

//    CardStack<Card> getDiscardPile();
//    void discardCard(Card card);
}
