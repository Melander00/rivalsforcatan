package samuel.card.util;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.center.CityCard;
import samuel.card.center.SettlementCard;
import samuel.card.center.SettlementLike;
import samuel.card.region.RegionCard;

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
        // todo: add method to get neighbouring regions
        return List.of();
    }
}
