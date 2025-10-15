package samuel.card;

import samuel.board.BoardPosition;
import samuel.card.util.ExpansionCardHelper;

public interface ExpansionCard extends PlaceableCard {


    @Override
    default boolean validatePlacement(BoardPosition position) {
        if(!position.isEmpty()) return false;
        return ExpansionCardHelper.validatePlacement(position);
    }
}
