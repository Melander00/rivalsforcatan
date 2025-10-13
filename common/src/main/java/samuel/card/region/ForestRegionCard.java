package samuel.card.region;

import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.Subscribe;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.resources.TimberResource;
import samuel.card.CardID;

public class ForestRegionCard extends AbstractRegionCard {

    private static final CardID id = new CardID("region", "forest");

    @Override
    public CardID getCardID() {
        return id;
    }

    public ForestRegionCard(int diceRoll) {
        super(TimberResource.class, diceRoll);
    }

    @Subscribe
    public void onProductionDice(ProductionDieEvent.Post event) {
        if(event.getRollResults() == getDiceRoll()) {
            increaseResource(1);
        }
    }
}
