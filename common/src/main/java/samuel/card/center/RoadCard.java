package samuel.card.center;

import samuel.board.BoardPosition;
import samuel.card.PlaceableCard;
import samuel.card.PriceTag;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.OreResource;
import samuel.resource.ResourceBundle;
import samuel.card.CardID;

import java.util.UUID;

public class RoadCard implements PlaceableCard, PriceTag {

    static final CardID id = new CardID("center", "road");

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
        if(!position.getBoard().isCenterRow(position)) return false;
        if(!position.isEmpty()) return false;

        PlaceableCard left = position.getBoard().getLeftOfPosition(position).getCard();
        if(left instanceof SettlementLike) return true;

        PlaceableCard right = position.getBoard().getRightOfPosition(position).getCard();
        if(right instanceof SettlementLike) return true;

        return false;
    }

    @Override
    public void onPlace(Player owner, GameContext context, BoardPosition position) {

    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle cost = new ResourceBundle();
        cost.addResource(OreResource.class, 1);
        cost.addResource(BrickResource.class,  2);
        return cost;
    }

    @Override
    public boolean canPlay(Player player, GameContext context) {
        if(!player.hasResources(getCost())) return false;
        return true;
    }
}
