package samuel.card.util;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.center.SettlementLike;
import samuel.card.region.RegionCard;

import java.util.ArrayList;
import java.util.List;

public class ExpansionCardHelper {

    /**
     * Doesn't check if the position is empty
     * @param position
     * @return
     */
    public static boolean validatePlacement(BoardPosition position) {
        Board board = position.getBoard();

        if(board.isCenterRow(position)) return false;

        List<BoardPosition> column = board.getColumnOfPosition(position);

        boolean hasSettlement = false;
        int slots = 0;
        int thisIndex = 0;
        int settlementIndex = 0;
        for(int index = 0; index < column.size(); index++) {

            BoardPosition pos = column.get(index);

            if(pos.equals(position)) {
                thisIndex = index;
            }

            if(pos.getCard() instanceof SettlementLike settlement) {
                slots = settlement.getExpansionSlots();
                settlementIndex = index;
                hasSettlement = true;
            }

        }
        if(!hasSettlement) return false;

        // distance in positions between settlement and this
        return Math.abs(settlementIndex - thisIndex) <= slots;
    }

    public static List<RegionCard> getNeighbouringRegions(BoardPosition position) {
        Board board = position.getBoard();

        if(board.isCenterRow(position)) return List.of();

        List<RegionCard> regions = new ArrayList<>();

        int positionsUnder = getPositionsUnderCenter(position);

        if(positionsUnder == 0) return List.of();

        boolean isAbove = positionsUnder < 0;

        BoardPosition left = board.getLeftOfPosition(position);
        if(left != null) {
            List<BoardPosition> column = board.getColumnOfPosition(left);
            regions.addAll(findRegionsInColumn(column, isAbove));
        }

        BoardPosition right = board.getRightOfPosition(position);
        if(right != null) {
            List<BoardPosition> column = board.getColumnOfPosition(right);
            regions.addAll(findRegionsInColumn(column, isAbove));
        }

        return regions;
    }

    private static List<RegionCard> findRegionsInColumn(List<BoardPosition> column, boolean above) {
        List<RegionCard> regions = new ArrayList<>();
        boolean foundSettlement = above;
        for(BoardPosition pos : column) {

            if(pos.getBoard().isCenterRow(pos)) {
                if(foundSettlement) break;
                foundSettlement = true;
                continue;
            }

            if(pos.isEmpty()) continue;

            if(!foundSettlement) continue;

            if(pos.getCard() instanceof RegionCard region) {
                regions.add(region);
            }
        }
        return regions;
    }

    /**
     *
     * @param position
     * @return negative if position is above settlement
     */
    public static int getPositionsUnderCenter(BoardPosition position) {
        Board board = position.getBoard();

        if(board.isCenterRow(position)) return 0;

        List<BoardPosition> column = board.getColumnOfPosition(position);

        int thisIndex = 0;
        int settlementIndex = 2;
        for(int index = 0; index < column.size(); index++) {

            BoardPosition pos = column.get(index);

            if(pos.equals(position)) {
                thisIndex = index;
            }

            if(board.isCenterRow(pos)) {
                settlementIndex = index;
            }
        }

        return thisIndex - settlementIndex;
    }
}
