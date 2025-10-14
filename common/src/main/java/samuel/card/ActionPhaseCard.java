package samuel.card;

import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;

public interface ActionPhaseCard extends PlayableCard {
    @Override
    default boolean canPlay(Player player, GameContext context) {
        return context.getPhase().equals(Phase.ACTION);
    }
}
