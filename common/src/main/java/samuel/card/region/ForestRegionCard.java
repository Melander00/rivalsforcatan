package samuel.card.region;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.player.Player;
import samuel.resource.TimberResource;

public class ForestRegionCard extends AbstractRegionCard<TimberResource> {

    private static final String name = "Forest";
    private static final String description = "Gains resources based on production die roll. 1 building site for brown.";

    public ForestRegionCard(int diceRoll) {
        super(new TimberResource(), diceRoll);
    }

    @Override
    public void onPlace(Player owner, Board board, BoardPosition position) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
