package samuel.board;

import samuel.card.PlaceableCard;
import samuel.util.CardID;

import java.util.Collection;
import java.util.List;

public class IntroductoryPrincipality implements Board {

    // For JSON serializable
    public IntroductoryPrincipality() {}

    @Override
    public boolean canPlace(PlaceableCard card, BoardPosition position) {
        return false;
    }

    @Override
    public void place(PlaceableCard card, BoardPosition position) {

    }

    @Override
    public Collection<BoardPosition> getPositionsById(CardID id) {
        return List.of();
    }

    @Override
    public boolean existsById(CardID id) {
        return false;
    }
}
