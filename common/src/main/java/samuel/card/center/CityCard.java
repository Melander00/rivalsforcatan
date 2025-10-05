package samuel.card.center;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.PlaceableCard;
import samuel.card.PointHolder;
import samuel.card.PriceTag;
import samuel.player.Player;
import samuel.point.Point;
import samuel.resource.GrainResource;
import samuel.resource.OreResource;
import samuel.resource.ResourceBundle;

import java.util.Collection;
import java.util.List;

public class CityCard implements PlaceableCard, PriceTag, PointHolder, SettlementLike {

    public static final String name = "City";
    public static final String description = "Place card on a settlement. 4 building sites for red and green.";

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
        return false;
    }

    @Override
    public void onPlace(Player owner, Board board, BoardPosition position) {

    }

    @Override
    public Collection<? extends Point> getPoints() {
        return List.of();
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle cost = new ResourceBundle();
        cost.add(new GrainResource(), 2);
        cost.add(new OreResource(),  4);
        return cost;
    }
}
