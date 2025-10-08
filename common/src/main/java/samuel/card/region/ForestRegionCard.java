package samuel.card.region;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.player.Player;
import samuel.resource.TimberResource;
import samuel.util.CardID;

public class ForestRegionCard extends AbstractRegionCard<TimberResource> {

    private static final CardID id = new CardID("region", "forest");

    @Override
    public CardID getCardID() {
        return id;
    }

    public ForestRegionCard(int diceRoll) {
        super(new TimberResource(), diceRoll);
    }

    @Override
    public void onPlace(Player owner, Board board, BoardPosition position) {

    }


}
