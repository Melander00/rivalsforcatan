package samuel.card.stack;

import samuel.card.Card;
import samuel.card.PlaceableCard;
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

    CardStack<Card> getBasicStack(int stackIndex);

    void addToBasicStacks(CardStack<Card> stack);

    CardStack<Card> getThemeStack(int stackIndex);

    void addToThemeStacks(CardStack<Card> stack);

    List<CardStack<Card>> getBasicStacks();
    List<CardStack<Card>> getThemeStacks();
}
