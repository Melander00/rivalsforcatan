package samuel.introductory;

import samuel.board.Board;
import samuel.card.center.RoadCard;
import samuel.card.center.SettlementCard;
import samuel.card.region.ForestRegionCard;
import samuel.game.GameContext;
import samuel.player.Player;

public class IntrodoctoryPrincipality {

    public static void setupPrincipality(Player player, GameContext context) {
        Board grid = player.getPrincipality();

        int rows = grid.getBoardPositions().size();
        if(rows == 0) {
            throw new IllegalArgumentException("The player board cannot be empty lmao");
        }

        int cols = grid.getBoardPositions().getFirst().size();

        int centerRow = rows / 2;
        int centerCol = cols / 2;

        // todo fix this hardcoded shit

        // begin with forcing road, all the rest should be validated

        // Road can only be placed next to settlement/city, settlement can only be placed next to road
        // stalemate => force road without validation.

        RoadCard road = new RoadCard();
        grid.getPositionFromGrid(centerRow, centerCol).setCard(road);
        road.onPlace(player, context);
        player.placeCard(new SettlementCard(), grid.getPositionFromGrid(centerRow, centerCol - 1), context);
        player.placeCard(new SettlementCard(), grid.getPositionFromGrid(centerRow, centerCol + 1), context);

        player.placeCard(new ForestRegionCard(2), grid.getPositionFromGrid(centerRow - 1, centerCol - 2), context);
        player.placeCard(new ForestRegionCard(1), grid.getPositionFromGrid(centerRow - 1, centerCol), context);
        player.placeCard(new ForestRegionCard(6), grid.getPositionFromGrid(centerRow - 1, centerCol + 2), context);
        player.placeCard(new ForestRegionCard(3), grid.getPositionFromGrid(centerRow + 1, centerCol - 2), context);
        player.placeCard(new ForestRegionCard(4), grid.getPositionFromGrid(centerRow + 1, centerCol), context);
        player.placeCard(new ForestRegionCard(5), grid.getPositionFromGrid(centerRow + 1, centerCol + 2), context);

    }
}
