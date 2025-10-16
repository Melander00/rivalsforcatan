package samuel.card.building;

import samuel.card.CardID;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.LumberResource;
import samuel.resource.resources.WoolResource;

import java.util.UUID;

public class StorehouseBuildingCard implements BuildingCard {

    private static final CardID id = new CardID("building", "storehouse");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public boolean canPlay(Player player, GameContext context) {
        return !context.getPhase().equals(Phase.ACTION);
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
        bundle.addResource(WoolResource.class, 1);
        return bundle;
    }

    // todo: implement
}
