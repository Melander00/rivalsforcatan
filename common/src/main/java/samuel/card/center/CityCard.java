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

    @Override
    public CardID getCardID() {
        return id;
    }
}
