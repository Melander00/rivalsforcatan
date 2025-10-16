package samuel.card.building;

import samuel.card.CardID;
import samuel.card.SingletonCard;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.GrainResource;

import java.util.UUID;

public class ParishHallBuildingCard implements BuildingCard, SingletonCard {

    private static final CardID id = new CardID("building", "parish_hall");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public boolean canPlay(Player player, GameContext context) {
        if(!context.getPhase().equals(Phase.ACTION)) return false;
        return !player.getPrincipality().existsById(id); // <- only one per principality
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
        bundle.addResource(BrickResource.class, 1);
        bundle.addResource(GrainResource.class, 1);
        return bundle;
    }

    // todo: implement
}
