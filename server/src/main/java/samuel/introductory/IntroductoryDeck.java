package samuel.introductory;

import samuel.card.Card;
import samuel.card.PlayableCard;
import samuel.card.action.*;
import samuel.card.building.TollBridge;
import samuel.card.event.*;
import samuel.card.hero.*;
import samuel.card.region.ForestRegionCard;
import samuel.card.region.RegionCard;
import samuel.card.ship.*;
import samuel.deck.Deck;

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

    private final List<PlayableCard> basicCards = List.of(
            // Buildings
            new TollBridge(),
            new TollBridge(),

            new TollBridge(),
            new TollBridge(),
            new TollBridge(),

            new TollBridge(),
            new TollBridge(),
            new TollBridge(),

            new TollBridge(),
            new TollBridge(),
            new TollBridge(),

            new TollBridge(),
            new TollBridge(),
            new TollBridge(),


            // Ships
            new LargeTradeShipCard(),
            new GoldShipCard(),
            new OreShipCard(),
            new GrainShipCard(),
            new LumberShipCard(),
            new BrickShipCard(),
            new WoolShipCard(),

            // Heroes
            new AustinHeroCard(),
            new HaraldHeroCard(),
            new IngaHeroCard(),
            new OsmundHeroCard(),
            new CandamirHeroCard(),
            new SiglindHeroCard(),

            // Action
            new BrigittaTheWiseWomanActionCard(),
            new BrigittaTheWiseWomanActionCard(),
            new RelocationActionCard(),
            new ScoutActionCard(),
            new ScoutActionCard(),
            new MerchantCaravanActionCard(),
            new MerchantCaravanActionCard(),
            new GoldsmithActionCard(),
            new GoldsmithActionCard()
    );

    private final List<EventCard> eventCards = List.of(
            new InventionEventCard(),
            new YuleEventCard(),
            new YearOfPlentyEventCard(),
            new YearOfPlentyEventCard(),
            new FraternalFeudsEventCard(),
            new FeudEventCard(),
            new TravelingMerchantEventCard(),
            new TravelingMerchantEventCard(),
            new TradeShipsRaceEventCard()
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
    public List<PlayableCard> getBasicCards() {
        return basicCards;
    }

    @Override
    public List<PlayableCard> getThemeCards() {
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
