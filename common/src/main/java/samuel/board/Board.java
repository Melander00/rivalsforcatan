package samuel.board;

import samuel.card.PlaceableCard;
import samuel.card.CardID;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public interface Board extends Iterable<BoardPosition> {

    /**
     * Returns a boolean whether the card can be placed on that position or not.
     * @param card
     * @param position
     * @return
     */
    boolean canPlace(PlaceableCard card, BoardPosition position);

    /**
     * Places the card on the position.
     * Requires to check if possible before running (keep open for testability)
     * @param card
     * @param position
     */
    void place(PlaceableCard card, BoardPosition position);

    /**
     * Returns all positions that has a card with the supplied cardId.
     * @param id
     * @return
     */
    List<BoardPosition> getPositionsById(CardID id);

    /**
     * Checks if the board contains a card of the supplied id.
      * @param id
     * @return
     */
    boolean existsById(CardID id);


    /**
     * Gets the position to the left of the supplied one.
     * @param position
     * @return
     */
    BoardPosition getLeftOfPosition(BoardPosition position);

    /**
     * Gets the position to the right of the supplied one.
     * @param position
     * @return
     */
    BoardPosition getRightOfPosition(BoardPosition position);

    /**
     * Gets the row of the supplied position.
     * @param position
     * @return
     */
    List<BoardPosition> getRowOfPosition(BoardPosition position);

    /**
     * Gets the column of the supplied position.
     * @param position
     * @return
     */
    List<BoardPosition> getColumnOfPosition(BoardPosition position);

    /**
     * Whether the supplied position is the center row
     * @param position
     * @return
     */
    boolean isCenterRow(BoardPosition position);

    /**
     * Returns a grid list of all the positions. Doesn't enforce grid implementation but helps with how the game is built.
     * @return
     */
    List<List<BoardPosition>> getBoardPositions();


    /**
     * Doesn't enforce grid board but helps immensely with position validation and placement.
     * @param row
     * @param column
     * @return
     */
    BoardPosition getPositionFromGrid(int row, int column);
}
