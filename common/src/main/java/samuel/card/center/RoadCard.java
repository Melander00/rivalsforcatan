package samuel.card.center;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.PlaceableCard;
import samuel.card.PriceTag;
import samuel.player.Player;
import samuel.resource.BrickResource;
import samuel.resource.OreResource;
import samuel.resource.ResourceBundle;

public class RoadCard implements PlaceableCard, PriceTag {

    private static final String name = "Road";
    private static final String description = "Allows construction of a new settlement and of road complements.";

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
        if(!position.isCenterRow()) return false;
        if(!position.isEmpty()) return false;

        PlaceableCard left = position.getLeft().getCard();
        if(left instanceof SettlementLike) return true;

        PlaceableCard right = position.getRight().getCard();
        if(right instanceof SettlementLike) return true;

        return false;
    }

    @Override
    public void onPlace(Player owner, Board board, BoardPosition position) {

    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle cost = new ResourceBundle();
        cost.add(new OreResource(), 1);
        cost.add(new BrickResource(),  2);
        return cost;
    }
}
