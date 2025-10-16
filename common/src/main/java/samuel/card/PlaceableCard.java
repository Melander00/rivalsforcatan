package samuel.card;

import samuel.board.BoardPosition;
import samuel.game.GameContext;
import samuel.player.Player;

public interface PlaceableCard extends Card, PlayableCard {
    boolean validatePlacement(BoardPosition position);

    /**
     * Called when the player places this card on a position.
     *
     * @param owner
     * @param context
     * @param position
     */
    default void onPlace(Player owner, GameContext context, BoardPosition position) {}

    /**
     * Called when a player removes this card from play.
     * @param context
     */
    default void onRemove(GameContext context) {}

//    @Override
//    default void play(Player player, GameContext context) {
//        BoardPosition position = null;
//        boolean isValid = false;
//        while(!isValid) {
//            position = player.requestBoardPosition(player.getPrincipality().getBoardPositions(), RequestCause.PLACE_CARD);
//            isValid = validatePlacement(position);
//            if(!isValid) {
//                player.directMessage("Invalid Placement");
//            }
//        }
//
//        player.placeCard(this, position, context);
//    }
}
