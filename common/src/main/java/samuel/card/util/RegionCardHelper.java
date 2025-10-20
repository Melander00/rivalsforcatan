package samuel.card.util;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.center.SettlementLike;
import samuel.card.region.RegionCard;

import java.util.List;

public class RegionCardHelper {

    /**
     * Doesn't check if the position is empty
     * @param position
     * @return
     */
    public static boolean validatePlacement(BoardPosition position) {
        Board board = position.getBoard();

        if(board.isCenterRow(position)) return false;

        List<BoardPosition> column = board.getColumnOfPosition(position);
        BoardPosition center = null;
        for(BoardPosition pos : column) {
            if(board.isCenterRow(pos)) {
                center = pos;
                break;
            }
        }
        if(center == null) throw new IllegalStateException("There is no center row in this column");

        BoardPosition left = board.getLeftOfPosition(center);
        if(!left.isEmpty() && left.getCard() instanceof SettlementLike) return true;

        BoardPosition right = board.getRightOfPosition(center);
        if(!right.isEmpty() && right.getCard() instanceof SettlementLike) return true;

        return false;
    }
}
