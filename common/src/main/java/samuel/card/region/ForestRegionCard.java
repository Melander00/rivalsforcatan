package samuel.card.region;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.player.Player;
import samuel.resource.resources.TimberResource;
import samuel.card.CardID;

public class ForestRegionCard extends AbstractRegionCard {

    private static final CardID id = new CardID("region", "forest");

    @Override
    public CardID getCardID() {
        return id;
    }

    public ForestRegionCard(int diceRoll) {
        super(TimberResource.class, diceRoll);
    }

    @Override
    public void onPlace(Player owner, Board board, BoardPosition position) {

    }


}
