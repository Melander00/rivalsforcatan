package samuel.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import samuel.card.Card;
import samuel.card.PlaceableCard;

import java.util.List;
import java.util.UUID;

public class GridPosition implements BoardPosition {

    @JsonIgnore
    private GridBoard board;

    private UUID uuid = UUID.randomUUID();

    private PlaceableCard card;

    private int row;

    private int column;

    // Required by jackson
    public GridPosition() {

    }

    public GridPosition(GridBoard board, int row, int column) {
        this.board = board;
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean isEmpty() {
        return this.card == null;
    }

    @Override
    public PlaceableCard getCard() {
        return this.card;
    }

    @Override
    public void setCard(PlaceableCard card) {
        this.card = card;
    }

    @Override
    public Board getBoard() {
        return this.board;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
