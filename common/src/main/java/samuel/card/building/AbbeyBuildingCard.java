package samuel.card.building;

import samuel.card.CardID;
import samuel.card.PointHolder;
import samuel.card.SingletonCard;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.point.PointBundle;
import samuel.point.points.ProgressPoint;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.OreResource;

import java.util.UUID;

public class AbbeyBuildingCard implements BuildingCard, SingletonCard, PointHolder {

    private static final CardID id = new CardID("building", "abbey");

    private final UUID uuid = UUID.randomUUID();

    // todo: all cards with price tag need to add to canPlay with getCost

    @Override
    public boolean canPlay(Player player, GameContext context) {
        boolean phase = context.getPhase().equals(Phase.ACTION);
        boolean canPay = player.hasResources(getCost());
        boolean unique = !player.getPrincipality().existsById(id);

        return phase && canPay && unique;
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
    public PointBundle getPoints() {
        PointBundle bundle = new PointBundle();
        bundle.addPoint(ProgressPoint.class, 1);
        return bundle;
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(BrickResource.class, 1);
        bundle.addResource(GrainResource.class, 1);
        bundle.addResource(OreResource.class, 1);
        return bundle;
    }
}
