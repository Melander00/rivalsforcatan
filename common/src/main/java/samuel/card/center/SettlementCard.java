package samuel.card.center;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.PlaceableCard;
import samuel.card.PointHolder;
import samuel.card.PriceTag;
import samuel.player.Player;
import samuel.point.Point;
import samuel.point.VictoryPoint;
import samuel.resource.*;
import samuel.card.CardID;

import java.util.Collection;

public class SettlementCard implements PlaceableCard, PriceTag, PointHolder, SettlementLike {

    private static final CardID id = new CardID("center", "settlement");

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
    public Collection<VictoryPoint> getPoints() {
        return VictoryPoint.create(1);
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle cost = new ResourceBundle();
        cost.add(new OreResource(), 1);
        cost.add(new BrickResource(),  1);
        cost.add(new GrainResource(),  1);
        cost.add(new WoolResource(),  1);
        return cost;
    }
}
