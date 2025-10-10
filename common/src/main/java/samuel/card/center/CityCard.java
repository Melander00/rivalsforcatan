package samuel.card.center;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.PlaceableCard;
import samuel.card.PointHolder;
import samuel.card.PriceTag;
import samuel.player.Player;
import samuel.point.Point;
import samuel.point.PointBundle;
import samuel.point.points.VictoryPoint;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.OreResource;
import samuel.resource.ResourceBundle;
import samuel.card.CardID;

import java.util.Collection;
import java.util.List;

public class CityCard implements PlaceableCard, PriceTag, PointHolder, SettlementLike {

    public static final CardID id = new CardID("center", "city");

    @Override
    public boolean validatePlacement(BoardPosition position) {
        return false;
    }

    @Override
    public void onPlace(Player owner, Board board, BoardPosition position) {

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
        cost.addResource(OreResource.class,  4);
        return cost;
    }

    @Override
    public CardID getCardID() {
        return id;
    }
}
