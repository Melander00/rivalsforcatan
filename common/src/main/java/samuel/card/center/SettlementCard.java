package samuel.card.center;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.PlaceableCard;
import samuel.card.PointHolder;
import samuel.card.PriceTag;
import samuel.player.Player;
import samuel.point.VictoryPoint;
import samuel.resource.*;

import java.util.Collection;

public class SettlementCard implements PlaceableCard, PriceTag, PointHolder, SettlementLike {

    private static final String name = "Settlement";
    private static final String description = "Two new regions. 2 building sites for green.";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean validatePlacement(BoardPosition position) {
        if(!position.isEmpty()) return false;
        if(!position.isCenterRow()) return false;

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
