package samuel.board;

import samuel.card.PlaceableCard;

import java.util.List;

public interface BoardPosition {

    boolean isEmpty();
    boolean isCenterRow();
    List<BoardPosition> getRow();
    List<BoardPosition> getColumn();

    BoardPosition getLeft();
    BoardPosition getRight();
    PlaceableCard getCard();

    Board getBoard();

}
