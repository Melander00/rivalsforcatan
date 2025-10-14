package samuel.card.region;

import samuel.card.CardID;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.Subscribe;
import samuel.resource.resources.BrickResource;

public class HillsRegionCard extends AbstractRegionCard {

    private static final CardID id = new CardID("region", "hills");

    @Override
    public CardID getCardID() {
        return id;
    }

    public HillsRegionCard(int diceRoll) {
        super(BrickResource.class, diceRoll);
    }

    @Subscribe
    public void onProductionDice(ProductionDieEvent.Post event) {
        if(event.getRollResults() == getDiceRoll()) {
            increaseResource(1);
        }
    }
}
