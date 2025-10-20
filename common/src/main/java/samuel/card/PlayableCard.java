package samuel.card;

import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;

/**
 * A general card that can be played in some way by a player.
 */
public interface PlayableCard extends Card {

    /**
     * Whether the player can play at this time.
     * @param player
     * @param context
     * @return
     */
    boolean canPlay(Player player, GameContext context);

//    void play(Player player, GameContext context);

    /**
     * Called when the player plays this card.
     * @param player
     * @param context
     */
    default void onPlay(Player player, GameContext context) {}

}
