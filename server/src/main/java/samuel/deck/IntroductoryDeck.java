package samuel.deck;

import samuel.card.Card;
import samuel.card.action.BrigittaTheWiseWoman;
import samuel.card.event.EventCard;
import samuel.card.event.InventionEventCard;
import samuel.card.region.ForestRegionCard;
import samuel.card.region.RegionCard;

import java.util.List;

public class IntroductoryDeck implements Deck {

    private final List<RegionCard> regionCards = List.of(
            new ForestRegionCard(2),
            new ForestRegionCard(1),
            new ForestRegionCard(2),
            new ForestRegionCard(3),
            new ForestRegionCard(4),
            new ForestRegionCard(5),
            new ForestRegionCard(6)
    );

    private final List<Card> basicCards = List.of(
            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),

            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),

            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),

            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),

            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman(),
            new BrigittaTheWiseWoman()
    );

    private final List<EventCard> eventCards = List.of(
            new InventionEventCard(),
            new InventionEventCard(),
            new InventionEventCard(),
            new InventionEventCard(),
            new InventionEventCard(),
            new InventionEventCard()
    );


    @Override
    public List<RegionCard> getRegionCards() {
        return regionCards;
    }

    @Override
    public List<EventCard> getEventCards() {
        return eventCards;
    }

    @Override
    public List<Card> getBasicCards() {
        return basicCards;
    }

    @Override
    public List<Card> getThemeCards() {
        return null;
    }

    @Override
    public List<Card> getFaceUpThemeCards() {
        return null;
    }

    @Override
    public int getAmountOfRoadCards() {
        return 7; // originally 9 but each player starts with 1
    }

    @Override
    public int getAmountOfSettlementCards() {
        return 5; // originally 9 but each player starts with 2
    }

    @Override
    public int getAmountOfCityCards() {
        return 7;
    }
}
