package samuel.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import samuel.card.PlaceableCard;
import samuel.card.CardID;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Simple concrete board that implements a grid using arrays.
 */
public class GridBoard implements Board {

    private HorizontalExpandableGrid grid;

    public GridBoard() {}

    public void initGrid(int rows, int cols) {
        this.grid = new HorizontalExpandableGrid();
        this.grid.initGrid(this, rows, cols);
    }

    public static GridBoard createGridBoard(int rows, int cols) {
        GridBoard board = new GridBoard();
        board.initGrid(rows, cols);
        return board;
    }

    @Override
    public boolean canPlace(PlaceableCard card, BoardPosition position) {
        return card.validatePlacement(position);
    }

    @Override
    public void place(PlaceableCard card, BoardPosition position) {
        position.setCard(card);
        this.ensureSize();
    }

    public void ensureSize() {
        this.grid.ensureSize();
    }

    @Override
    public BoardPosition getPositionFromGrid(int row, int column) {
        return this.grid.getPositionFromGrid(row, column);
    }

    @Override
    public List<BoardPosition> getPositionsById(CardID id) {
        List<BoardPosition> positions = new ArrayList<>();

        for(BoardPosition pos : this.grid) {
            if(pos.getCard().getCardID().equals(id)) {
                positions.add(pos);
            }
        }

        return positions;
    }

    @Override
    public boolean existsById(CardID id) {
        for(BoardPosition pos : this.grid) {
            if(pos.isEmpty()) continue;
            if(pos.getCard().getCardID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BoardPosition getLeftOfPosition(BoardPosition position) {
        return this.grid.getLeftOfPosition(position);
    }

    @Override
    public BoardPosition getRightOfPosition(BoardPosition position) {
        return this.grid.getRightOfPosition(position);
    }

    @Override
    public List<BoardPosition> getRowOfPosition(BoardPosition position) {
        return this.grid.getRowFromPosition(position);
    }

    @Override
    public List<BoardPosition> getColumnOfPosition(BoardPosition position) {
        return this.grid.getColumnFromPosition(position);
    }

    @Override
    public boolean isCenterRow(BoardPosition position) {
        return this.grid.getCenterRow().contains(position);
    }


    @Override
    public List<List<BoardPosition>> getBoardPositions() {
        return this.grid.getGrid();
    }

    @Override
    public Iterator<BoardPosition> iterator() {
        return this.grid.iterator();
    }
}
