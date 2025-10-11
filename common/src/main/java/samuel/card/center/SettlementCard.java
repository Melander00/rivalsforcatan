package samuel.card.center;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.PlaceableCard;
import samuel.card.PointHolder;
import samuel.card.PriceTag;
import samuel.player.Player;
import samuel.point.PointBundle;
import samuel.point.points.VictoryPoint;
import samuel.resource.*;
import samuel.card.CardID;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.OreResource;
import samuel.resource.resources.WoolResource;

import java.util.Collection;
import java.util.UUID;

public class SettlementCard implements PlaceableCard, PriceTag, PointHolder, SettlementLike {

    private static final CardID id = new CardID("center", "settlement");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public CardID getCardID() {
        return id;
    }

    @Override
    public boolean validatePlacement(BoardPosition position) {
        if(!position.isEmpty()) return false;
        if(!position.getBoard().isCenterRow(position)) return false;

        // check if either left or right is Road
        return true;

    }

    @Override
    public void onPlace(Player owner, Board board, BoardPosition position) {

    }


    @Override
    public PointBundle getPoints() {
        PointBundle bundle = new PointBundle();
        bundle.addPoint(VictoryPoint.class, 1);
        return bundle;
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle cost = new ResourceBundle();
        cost.addResource(OreResource.class, 1);
        cost.addResource(BrickResource.class,  1);
        cost.addResource(GrainResource.class,  1);
        cost.addResource(WoolResource.class,  1);
        return cost;
    }
}
