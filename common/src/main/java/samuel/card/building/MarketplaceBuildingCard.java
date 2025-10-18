package samuel.card.building;

import samuel.card.CardID;
import samuel.card.PointHolder;
import samuel.card.SingletonCard;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.point.PointBundle;
import samuel.point.points.CommercePoint;
import samuel.point.points.ProgressPoint;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.WoolResource;

import java.util.UUID;

public class MarketplaceBuildingCard implements BuildingCard, SingletonCard, PointHolder {

    private static final CardID id = new CardID("building", "marketplace");

    private final UUID uuid = UUID.randomUUID();

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
        bundle.addPoint(CommercePoint.class, 1);
        return bundle;
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(WoolResource.class, 1);
        bundle.addResource(GrainResource.class, 1);
        return bundle;
    }

    // todo implement

}
