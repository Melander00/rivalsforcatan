package samuel.card;

import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;

public interface PlayableCard extends Card {

    boolean canPlay(Player player, GameContext context);

    void play(Player player, GameContext context);

}
