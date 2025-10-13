package samuel.stack;

import samuel.card.Card;
import samuel.card.PlaceableCard;
import samuel.card.PlayableCard;
import samuel.card.event.EventCard;
import samuel.card.region.RegionCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;

import java.util.ArrayList;
import java.util.List;

public class GenericStackContainer implements StackContainer {

    private final CardStack<PlaceableCard> roadStack = new GenericCardStack<>();
    private final CardStack<PlaceableCard> settlementStack = new GenericCardStack<>();
    private final CardStack<RegionCard> regionStack = new GenericCardStack<>();
    private final CardStack<PlaceableCard> cityStack = new GenericCardStack<>();
    private final CardStack<EventCard> eventStack = new EventCardStack();

    private final List<CardStack<PlayableCard>> basicStacks = new ArrayList<>();
    private final List<CardStack<PlayableCard>> themeStacks = new ArrayList<>();


    @Override
    public CardStack<PlaceableCard> getRoadStack() {
        return roadStack;
    }

    @Override
    public CardStack<PlaceableCard> getSettlementStack() {
        return settlementStack;
    }

    @Override
    public CardStack<RegionCard> getRegionStack() {
        return regionStack;
    }

    @Override
    public CardStack<PlaceableCard> getCityStack() {
        return cityStack;
    }

    @Override
    public CardStack<EventCard> getEventStack() {
        return eventStack;
    }

    @Override
    public CardStack<PlayableCard> getBasicStack(int stackIndex) {
        return basicStacks.get(stackIndex);
    }

    @Override
    public void addToBasicStacks(CardStack<PlayableCard> stack) {
        basicStacks.add(stack);
    }


    @Override
    public CardStack<PlayableCard> getThemeStack(int stackIndex) {
        return themeStacks.get(stackIndex);
    }

    @Override
    public void addToThemeStacks(CardStack<PlayableCard> stack) {
        themeStacks.add(stack);
    }

    @Override
    public List<CardStack<PlayableCard>> getBasicStacks() {
        return basicStacks;
    }

    @Override
    public List<CardStack<PlayableCard>> getThemeStacks() {
        return themeStacks;
    }
}
