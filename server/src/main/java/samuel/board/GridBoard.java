package samuel.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import samuel.card.PlaceableCard;
import samuel.card.CardID;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Simple concrete board that implements a grid using arrays.
 */
public class GridBoard implements Board {

    private List<List<GridPosition>> grid = new ArrayList<>();

    public GridBoard() {

    }

    public void initGrid() {
        // begin with 5x5 grid
        for(int r = 0; r < 5; r++) {
            List<GridPosition> row = new ArrayList<>();
            for(int c = 0; c < 5; c++) {
                row.add(new GridPosition(this, r, c));
            }
            this.grid.add(row);
        }
    }

    public static GridBoard createGridBoard() {
        GridBoard board = new GridBoard();
        board.initGrid();
        return board;
    }

    @Override
    public boolean canPlace(PlaceableCard card, BoardPosition position) {
        return card.validatePlacement(position);
    }

    @Override
    public void place(PlaceableCard card, BoardPosition position) {
        position.setCard(card);
        // check if board needs expansion
        this.ensureSize();
//        card.onPlace();
    }

    public void ensureSize() {
        // check leftmost
    }

    @Override
    public GridPosition getPositionFromGrid(int row, int column) {
        int rowSize = grid.size();
        if(rowSize == 0) return null;
        int columnSize = grid.getFirst().size();

        if(
                (row < 0 || row > rowSize - 1)
             || (column < 0 || column > columnSize - 1)

        ) return null;

        return grid.get(row).get(column);
    }

    @Override
    public List<BoardPosition> getPositionsById(CardID id) {
        List<BoardPosition> positions = new ArrayList<>();

        for(List<GridPosition> row : this.grid) {
            for(GridPosition pos : row) {
                if(pos.getCard().getCardID().equals(id)) {
                    positions.add(pos);
                }
            }
        }

        return positions;
    }

    @Override
    public boolean existsById(CardID id) {
        for(List<GridPosition> row : this.grid) {
            for(GridPosition pos : row) {
                if(pos.getCard().getCardID().equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }



    @Override
    public BoardPosition getLeftOfPosition(BoardPosition position) {
        return null;
    }

    @Override
    public BoardPosition getRightOfPosition(BoardPosition position) {
        return null;
    }

    @Override
    public List<BoardPosition> getRowOfPosition(BoardPosition position) {
        return List.of();
    }

    @Override
    public List<BoardPosition> getColumnOfPosition(BoardPosition position) {
        return List.of();
    }

    @Override
    public boolean isCenterRow(BoardPosition position) {
        return false;
    }

    @Override
    public List<BoardPosition> filterPositions(Predicate<BoardPosition> positionPredicate) {
        List<BoardPosition> positions = new ArrayList<>();

        for(List<GridPosition> row : this.grid) {
            for(GridPosition pos : row) {
                if(positionPredicate.test(pos)) {
                    positions.add(pos);
                }
            }
        }

        return positions;
    }

    @Override
    public List<BoardPosition> getBoardPositions() {

        List<BoardPosition> positions = new ArrayList<>();

        for(List<GridPosition> row : this.grid) {
            positions.addAll(row);
        }

        return positions;
    }

//    @JsonIgnore
//    public List<List<GridPosition>> getGrid() {
//        return this.grid;
//    }
}
