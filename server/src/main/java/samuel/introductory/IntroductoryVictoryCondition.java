package samuel.introductory;

import samuel.condition.VictoryCondition;
import samuel.player.Player;
import samuel.point.points.VictoryPoint;

public class IntroductoryVictoryCondition implements VictoryCondition {

    @Override
    public boolean hasWon(Player player) {
        return player.getPoints(VictoryPoint.class) >= 7;
    }
}
