package samuel.card;

import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;

public interface PlayableCard extends Card {

    default boolean canPlay(GameContext context) {
        return context.getPhase().equals(Phase.ACTION);
    }

    void play(Player player, GameContext context);

}
