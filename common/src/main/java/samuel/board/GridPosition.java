package samuel.board;

import samuel.card.PlaceableCard;

import java.util.List;

public class GridPosition implements BoardPosition {

    private GridBoard board;

    // Required by jackson
    public GridPosition() {

    }

    public GridPosition(GridBoard board) {
        this.board = board;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isCenterRow() {
        return false;
    }

    @Override
    public List<BoardPosition> getRow() {
        return List.of();
    }

    @Override
    public List<BoardPosition> getColumn() {
        return List.of();
    }

    @Override
    public BoardPosition getLeft() {
        return null;
    }

    @Override
    public BoardPosition getRight() {
        return null;
    }

    @Override
    public PlaceableCard getCard() {
        return null;
    }

    @Override
    public Board getBoard() {
        return this.board;
    }
}
