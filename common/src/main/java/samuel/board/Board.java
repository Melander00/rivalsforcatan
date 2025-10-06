package samuel.board;

import samuel.card.PlaceableCard;
import samuel.util.CardID;

import java.util.Collection;

public interface Board {

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
    Collection<BoardPosition> getPositionsById(CardID id);

    /**
     * Checks if the board contains a card of the supplied id.
      * @param id
     * @return
     */
    boolean existsById(CardID id);
}
