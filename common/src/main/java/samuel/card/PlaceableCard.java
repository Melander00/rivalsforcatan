package samuel.card;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.player.Player;

public interface PlaceableCard extends Card {
    boolean validatePlacement(BoardPosition position);
    void onPlace(Player owner, Board board, BoardPosition position);
}
