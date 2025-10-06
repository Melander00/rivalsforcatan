package samuel.board;

import samuel.card.PlaceableCard;
import samuel.util.CardID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple concrete board that implements a grid using arrays.
 */
public class GridBoard implements Board {

    private final List<List<GridPosition>> grid;

    public GridBoard() {
        this.grid = new ArrayList<>();

        // begin with 5x5 grid
        for(int r = 0; r < 5; r++) {
            List<GridPosition> row = new ArrayList<>();
            for(int c = 0; c < 5; c++) {
                row.add(new GridPosition(this));
            }
            this.grid.add(row);
        }
    }


    @Override
    public boolean canPlace(PlaceableCard card, BoardPosition position) {
        return card.validatePlacement(position);
    }

    @Override
    public void place(PlaceableCard card, BoardPosition position) {

    }

    public GridPosition getPositionOnGrid(int row, int column) {
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
    public Collection<BoardPosition> getPositionsById(CardID id) {
        return List.of();
    }

    @Override
    public boolean existsById(CardID id) {
        return false;
    }
}
