package samuel.principalities;

import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.board.GridPosition;
import samuel.card.center.RoadCard;
import samuel.card.center.SettlementCard;
import samuel.card.region.ForestRegionCard;

public class IntrodoctoryPrincipality {

    public static void setupPrincipality(Board grid) {
        int center = 2;

        // todo fix this hardcoded shit

        grid.getPositionFromGrid(center, 1).setCard(new SettlementCard());
        grid.getPositionFromGrid(center, 3).setCard(new SettlementCard());

        grid.getPositionFromGrid(center-1, 0).setCard(new ForestRegionCard(2));
        grid.getPositionFromGrid(center-1, 2).setCard(new ForestRegionCard(1));
        grid.getPositionFromGrid(center-1, 4).setCard(new ForestRegionCard(6));
        grid.getPositionFromGrid(center+1, 0).setCard(new ForestRegionCard(3));
        grid.getPositionFromGrid(center+1, 2).setCard(new ForestRegionCard(4));
        grid.getPositionFromGrid(center+1, 4).setCard(new ForestRegionCard(5));

        grid.place(new RoadCard(), grid.getPositionFromGrid(center, 2));

    }
}
