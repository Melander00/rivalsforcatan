package samuel.card.region;

import samuel.card.CardID;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.Subscribe;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.TimberResource;

public class FieldsRegionCard extends AbstractRegionCard {

    private static final CardID id = new CardID("region", "fields");

    @Override
    public CardID getCardID() {
        return id;
    }

    public FieldsRegionCard(int diceRoll) {
        super(GrainResource.class, diceRoll);
    }

    @Subscribe
    public void onProductionDice(ProductionDieEvent.Post event) {
        if(event.getRollResults() == getDiceRoll()) {
            increaseResource(1);
        }
    }


}
