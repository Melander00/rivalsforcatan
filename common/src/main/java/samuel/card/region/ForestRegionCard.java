package samuel.card.region;

import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.Subscribe;
import samuel.resource.resources.LumberResource;
import samuel.card.CardID;

public class ForestRegionCard extends AbstractRegionCard {

    private static final CardID id = new CardID("region", "forest");

    @Override
    public CardID getCardID() {
        return id;
    }

    public ForestRegionCard(int diceRoll) {
        super(LumberResource.class, diceRoll);
    }

    @Subscribe
    public void onProductionDice(ProductionDieEvent.Post event) {
        if(event.getRollResults() == getDiceRoll()) {
            increaseResource(1);
        }
    }


}
