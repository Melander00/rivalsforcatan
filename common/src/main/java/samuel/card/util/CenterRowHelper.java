package samuel.card.util;

import samuel.board.Board;
import samuel.board.BoardPosition;

public class CenterRowHelper {

    /**
     * Doesn't check if the position is empty
     * @param position
     * @return
     */
    public static boolean validatePlacement(BoardPosition position) {

        Board board = position.getBoard();

        return board.isCenterRow(position);

    }

}
