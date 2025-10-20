package samuel.card;

import samuel.board.BoardPosition;
import samuel.card.util.ExpansionCardHelper;

/**
 * Cards that can be placed above and below settlements/cities.
 */
public interface ExpansionCard extends PlaceableCard {


    @Override
    default boolean validatePlacement(BoardPosition position) {
        if(!position.isEmpty()) return false;
        return ExpansionCardHelper.validatePlacement(position);
    }
}
