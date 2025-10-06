package samuel.board;

import samuel.card.PlaceableCard;

import java.util.List;

public interface BoardPosition {

    /**
     * Returns whether the position has a card in it or not.
     * @return
     */
    boolean isEmpty();

    /**
     * Returns whether the position is in the center row (Settlements, Roads, Cities)
     * @return
     */
    boolean isCenterRow();

    /**
     * Returns all the positions across the row
     * @return
     */
    List<BoardPosition> getRow();

    /**
     * Returns all the positions along the column
     * @return
     */
    List<BoardPosition> getColumn();

    /**
     * Gets the position directly to the left of this one.
     * @return Null if it doesn't exist
     */
    BoardPosition getLeft();

    /**
     * Gets the position directly to the right of this one.
     * @return Null if it doesn't exist
     */
    BoardPosition getRight();

    /**
     * Gets the card placed on this position
     * @return Null if there is none
     */
    PlaceableCard getCard();

    /**
     * Returns the parent board
     * @return
     */
    Board getBoard();
}
