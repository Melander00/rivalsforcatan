package samuel.test.server.victorycondition;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.condition.VictoryCondition;
import samuel.game.GameContext;
import samuel.introductory.IntroductoryVictoryCondition;
import samuel.player.Player;
import samuel.point.points.VictoryPoint;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IntroductoryVictoryConditionTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    @Test
    void testMoreThan7() {
        VictoryCondition condition = new IntroductoryVictoryCondition();
        when(player.getPoints(VictoryPoint.class)).thenReturn(8);

        assertTrue(condition.hasWon(player));
    }

    @Test
    void testEqual7() {
        VictoryCondition condition = new IntroductoryVictoryCondition();
        when(player.getPoints(VictoryPoint.class)).thenReturn(7);

        assertTrue(condition.hasWon(player));
    }


    @Test
    void testLessThan7() {
        VictoryCondition condition = new IntroductoryVictoryCondition();
        when(player.getPoints(VictoryPoint.class)).thenReturn(6);

        assertFalse(condition.hasWon(player));
    }
}
