package samuel.board;

import samuel.card.PlaceableCard;

import java.util.List;
import java.util.UUID;

public interface BoardPosition {

    /**
     * Returns whether the position has a card in it or not.
     * @return
     */
    boolean isEmpty();

    /**
     * Gets the card placed on this position
     * @return null if there is none
     */
    PlaceableCard getCard();

    /**
     * Sets the card on this position.
     * @param card
     */
    void setCard(PlaceableCard card);

    /**
     * Returns the parent board
     * @return
     */
    Board getBoard();

    /**
     * Unique identifier
     * @return
     */
    UUID getUuid();

    /**
     * Unique identifier
     */
    void setUuid(UUID uuid);
}
