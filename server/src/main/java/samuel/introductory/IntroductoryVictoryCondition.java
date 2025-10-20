package samuel.introductory;

import samuel.condition.VictoryCondition;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.point.points.VictoryPoint;

public class IntroductoryVictoryCondition implements VictoryCondition {

    @Override
    public boolean hasWon(Player player, GameContext context) {
        return context.getVictoryPoints(player) >= 7;
    }
}
