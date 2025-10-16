package samuel.card.building.booster;

import samuel.card.CardID;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.Subscribe;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.LumberResource;
import samuel.resource.resources.OreResource;

import java.util.UUID;

public class GrainMillBuildingCard extends AbstractProductionBoosterCard {

    private static final CardID id = new CardID("building", "grain_mill");
    private final UUID uuid = UUID.randomUUID();


    public GrainMillBuildingCard() {
        super(GrainResource.class);
    }

    @Override
    public CardID getCardID() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(LumberResource.class, 1);
        bundle.addResource(GrainResource.class, 1);
        return bundle;
    }

    @Subscribe
    @Override
    public void onProductionEvent(ProductionDieEvent.Post event) {
        super.onProductionEvent(event);
    }
}

