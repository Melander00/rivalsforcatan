package samuel.card.region;

import samuel.card.CardID;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.Subscribe;
import samuel.resource.resources.OreResource;

public class MountainsRegionCard extends AbstractRegionCard {

    private static final CardID id = new CardID("region", "mountains");

    @Override
    public CardID getCardID() {
        return id;
    }

    public MountainsRegionCard(int diceRoll) {
        super(OreResource.class, diceRoll);
    }

    @Subscribe
    public void onProductionDice(ProductionDieEvent.Post event) {
        if(event.getRollResults() == getDiceRoll()) {
            increaseResource(1);
        }
    }


}
