package samuel.board;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Tanken är att vi ska kunna abstrahera bort hur grid:et fungerar
 * Vi ska kunna använda oss av
 *  method(row, column)
 *  method(0 -> n, 0 -> n)
 * som om det vore ett vanligt grid med index 0
 *
 * men i själva verket så ska vi ha en center column som är index 0
 * alla till vänster har negativa index
 * alla till höger har positiva index
 * så när vi lägger till en ny kolumn till vänster så får den ett negativt index
 * vilket gör att vi inte måste gå igenom alla grid positions för att reassigna var de ligger.
 *
 * Alt. vi tar bort row och kolumn från boardpositions och gör alla sånna beräkningar här
 */
public class HorizontalExpandableGrid implements Iterable<BoardPosition> {

    private final List<List<BoardPosition>> grid = new ArrayList<>();

    private Board board;

    public void initGrid(Board board, int rows, int cols) {
        this.board = board;
        // begin with rows X cols grid
        for(int r = 0; r < rows; r++) {
            List<BoardPosition> row = new ArrayList<>();
            for(int c = 0; c < cols; c++) {
                row.add(new GridPosition(board));
            }
            this.grid.add(row);
        }
    }

    public BoardPosition getPositionFromGrid(int row, int column) {
        int rowSize = grid.size();
        if(rowSize == 0) return null;
        int columnSize = grid.getFirst().size();

        if(
                (row < 0 || row > rowSize - 1) || (column < 0 || column > columnSize - 1)
        ) return null;

        return grid.get(row).get(column);
    }

    public void ensureSize() {
        List<BoardPosition> leftMost = getColumnByIndex(0);

        for(BoardPosition left : leftMost) {
            if(!left.isEmpty()) {

                // add another column to the left
                for(List<BoardPosition> row : this.grid) {
                    row.addFirst(new GridPosition(board));
                }

                break;
            }
        }

        List<BoardPosition> rightMost = getColumnByIndex(grid.getFirst().size()-1);
        for(BoardPosition right : rightMost) {
            if(!right.isEmpty()) {

                // add another column to the right
                for(List<BoardPosition> row : this.grid) {
                    row.addLast(new GridPosition(board));
                }

                break;
            }
        }
    }


    public List<BoardPosition> getRowFromPosition(BoardPosition position) {
        for (List<BoardPosition> row : this.grid) {
            for (int c = 0; c < row.size(); c++) {
                BoardPosition pos = row.get(c);
                if (pos.getUuid().equals(position.getUuid())) {
                    return row;
                }
            }
        }
        return null;
    }

    public List<BoardPosition> getColumnFromPosition(BoardPosition position) {
        for (List<BoardPosition> row : this.grid) {
            for (int c = 0; c < row.size(); c++) {
                BoardPosition pos = row.get(c);
                if (pos.getUuid().equals(position.getUuid())) {

                    return getColumnByIndex(c);

                }
            }
        }
        return null;
    }

    private List<BoardPosition> getColumnByIndex(int column) {
        List<BoardPosition> positions = new ArrayList<>();
        for(int r = 0; r < this.grid.size(); r++) {
            positions.add(getPositionFromGrid(r, column));
        }
        return positions;
    }

    public BoardPosition getLeftOfPosition(BoardPosition position) {
        for(int r = 0; r < this.grid.size(); r++) {
            List<BoardPosition> row = this.grid.get(r);
            for(int c = 0; c < row.size(); c++) {
                BoardPosition pos = row.get(c);
                if(pos.getUuid().equals(position.getUuid())) {

                    // if we are on the edge there is nothing on the left.
                    if(c == 0) return null;

                    return row.get(c-1);

                }
            }
        }
        return null;
    }

    public BoardPosition getRightOfPosition(BoardPosition position) {
        for(int r = 0; r < this.grid.size(); r++) {
            List<BoardPosition> row = this.grid.get(r);
            for(int c = 0; c < row.size(); c++) {
                BoardPosition pos = row.get(c);
                if(pos.getUuid().equals(position.getUuid())) {

                    // if we are on the edge, there is nothing to the right
                    if(c == row.size()-1) return null;

                    return row.get(c+1);

                }
            }
        }
        return null;
    }

    public List<List<BoardPosition>> getGrid() {
        return grid;
    }

    public List<BoardPosition> getCenterRow() {
        int rows = grid.size();
        int centerIndex = rows / 2;
        return grid.get(centerIndex);
    }

    @Override
    public Iterator<BoardPosition> iterator() {
        return new Iterator<>() {

            private int outerIndex = 0;
            private int innerIndex = 0;

            @Override
            public boolean hasNext() {
                while(outerIndex < grid.size()) {
                    if(innerIndex < grid.get(outerIndex).size()) {
                        return true;
                    } else {
                        outerIndex++;
                        innerIndex = 0;
                    }
                }
                return false;
            }

            @Override
            public BoardPosition next() {
                if(!hasNext()) {
                    return null;
                }
                return grid.get(outerIndex).get(innerIndex++);
            }
        };
    }
}
