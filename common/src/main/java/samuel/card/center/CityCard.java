package samuel.card.center;

import samuel.board.BoardPosition;
import samuel.card.PlaceableCard;
import samuel.card.PointHolder;
import samuel.card.PriceTag;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.point.PointBundle;
import samuel.point.points.VictoryPoint;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.OreResource;
import samuel.resource.ResourceBundle;
import samuel.card.CardID;

import java.util.UUID;

public class CityCard implements PlaceableCard, PriceTag, PointHolder, SettlementLike {

    private static final CardID id = new CardID("center", "city");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public boolean validatePlacement(BoardPosition position) {

        if(position.isEmpty()) return false;

        return position.getCard().getCardID().equals(SettlementCard.id); // todo: high coupling

    }

    @Override
    public void onPlace(Player owner, GameContext context) {

    }

    @Override
    public PointBundle getPoints() {
        PointBundle bundle = new PointBundle();
        bundle.addPoint(VictoryPoint.class, 2);
        return bundle;
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle cost = new ResourceBundle();
        cost.addResource(GrainResource.class, 2);
        cost.addResource(OreResource.class,  3);
        return cost;
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
    public int getExpansionSlots() {
        return 2;
    }

    @Override
    public boolean canPlay(Player player, GameContext context) {
        if(!player.hasResources(getCost())) return false;
        return true;
    }
}
