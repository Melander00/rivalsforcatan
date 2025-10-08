package samuel.card.center;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.PlaceableCard;
import samuel.card.PriceTag;
import samuel.player.Player;
import samuel.resource.BrickResource;
import samuel.resource.OreResource;
import samuel.resource.ResourceBundle;
import samuel.util.CardID;

public class RoadCard implements PlaceableCard, PriceTag {

    private static final CardID id = new CardID("center", "road");

    @Override
    public CardID getCardID() {
        return id;
    }

    @Override
    public boolean validatePlacement(BoardPosition position) {
        if(!position.getBoard().isCenterRow(position)) return false;
        if(!position.isEmpty()) return false;

        PlaceableCard left = position.getBoard().getLeftOfPosition(position).getCard();
        if(left instanceof SettlementLike) return true;

        PlaceableCard right = position.getBoard().getRightOfPosition(position).getCard();
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
