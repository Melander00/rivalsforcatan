package samuel.card.region;

import samuel.card.CardID;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.Subscribe;
import samuel.resource.resources.GoldResource;

public class GoldFieldRegionCard extends AbstractRegionCard {

    private static final CardID id = new CardID("region", "gold_field");

    @Override
    public CardID getCardID() {
        return id;
    }

    public GoldFieldRegionCard(int diceRoll) {
        super(GoldResource.class, diceRoll);
    }

    @Subscribe
    public void onProductionDice(ProductionDieEvent.Post event) {
        if(event.getRollResults() == getDiceRoll()) {
            increaseResource(1);
        }
    }


}
