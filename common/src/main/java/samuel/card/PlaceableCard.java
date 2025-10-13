package samuel.card;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.player.request.RequestCause;

public interface PlaceableCard extends Card, PlayableCard {
    boolean validatePlacement(BoardPosition position);
    default void onPlace(Player owner, Board board, BoardPosition position) {}

    @Override
    default void play(Player player, GameContext context) {
        BoardPosition position = null;
        boolean isValid = false;
        while(!isValid) {
            position = player.requestBoardPosition(player.getPrincipality().getBoardPositions(), RequestCause.PLACE_CARD);
            isValid = validatePlacement(position);
            if(!isValid) {
                player.directMessage("Invalid Placement");
            }
        }

        player.placeCard(position, this);
    }
}
