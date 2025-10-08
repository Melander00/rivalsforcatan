package samuel.condition;

import samuel.player.Player;
import samuel.point.VictoryPoint;

public class IntroductoryVictoryCondition implements VictoryCondition {

    @Override
    public boolean hasWon(Player player) {
        return player.getPoints(VictoryPoint.class) >= 7;
    }
}
