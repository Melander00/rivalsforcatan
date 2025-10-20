package samuel.introductory;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.center.RoadCard;
import samuel.card.center.SettlementCard;
import samuel.card.region.*;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.util.Pair;

import java.util.List;

public class IntrodoctoryPrincipality {

    private static final List<Integer> redRolls = List.of(2,1,6,3,4,5);
    private static final List<Integer> blueRolls = List.of(3,4,5,2,1,6);

    private static final List<Pair<Integer, Integer>> regionPositions = List.of(
            new Pair<>(-1, -2),
            new Pair<>(-1, 0),
            new Pair<>(-1, 2),
            new Pair<>(1, -2),
            new Pair<>(1, 0),
            new Pair<>(1, 2)
    );

    public static void setupPrincipality(Player player, int index, GameContext context) {
        Board grid = player.getPrincipality();


        int rows = grid.getBoardPositions().size();
        if(rows == 0) {
            throw new IllegalArgumentException("The player board cannot be empty lmao");
        }

        int cols = grid.getBoardPositions().getFirst().size();

        int centerRow = rows / 2;
        int centerCol = cols / 2;

        boolean isRed = index % 2 == 1; // every odd player is red.

        // begin with forcing road, all the rest should be validated

        // Road can only be placed next to settlement/city, settlement can only be placed next to road
        // stalemate => force road without validation.

        RoadCard road = new RoadCard();
        BoardPosition roadPosition = grid.getPositionFromGrid(centerRow, centerCol);
        roadPosition.setCard(road);
        road.onPlace(player, context, roadPosition);
        player.placeCard(new SettlementCard(), grid.getPositionFromGrid(centerRow, centerCol - 1), context);
        player.placeCard(new SettlementCard(), grid.getPositionFromGrid(centerRow, centerCol + 1), context);


        List<RegionCard> regions = getRegions(isRed ? redRolls : blueRolls);
        for(int i = 0; i < regionPositions.size(); i++) {
            Pair<Integer, Integer> offset = regionPositions.get(i);
            BoardPosition pos = grid.getPositionFromGrid(centerRow + offset.first(), centerCol + offset.second());
            RegionCard card = regions.get(i);
            player.placeCard(card, pos, context);
        }


//
//        player.placeCard(new ForestRegionCard(2), grid.getPositionFromGrid(centerRow - 1, centerCol - 2), context);
//        player.placeCard(new ForestRegionCard(1), grid.getPositionFromGrid(centerRow - 1, centerCol), context);
//        player.placeCard(new ForestRegionCard(6), grid.getPositionFromGrid(centerRow - 1, centerCol + 2), context);
//        player.placeCard(new ForestRegionCard(3), grid.getPositionFromGrid(centerRow + 1, centerCol - 2), context);
//        player.placeCard(new ForestRegionCard(4), grid.getPositionFromGrid(centerRow + 1, centerCol), context);
//        player.placeCard(new ForestRegionCard(5), grid.getPositionFromGrid(centerRow + 1, centerCol + 2), context);

    }


    private static List<RegionCard> getRegions(List<Integer> rolls) {
        // top-left to top-right then bot-left to bot-right
        // Forest, Gold Fields, Fields, Hill, Pasture, Mountains

        RegionCard forest = new ForestRegionCard(rolls.get(0));
        forest.increaseResource(1);

        RegionCard gold = new GoldFieldRegionCard(rolls.get(1));

        RegionCard fields = new FieldsRegionCard(rolls.get(2));
        fields.increaseResource(1);

        RegionCard hills = new HillsRegionCard(rolls.get(3));
        hills.increaseResource(1);

        RegionCard pasture = new PastureRegionCard(rolls.get(4));
        pasture.increaseResource(1);

        RegionCard mountains = new MountainsRegionCard(rolls.get(5));
        mountains.increaseResource(1);

        return List.of(forest, gold, fields, hills, pasture, mountains);
    }
}
