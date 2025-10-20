package samuel.test.server.introductory;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.condition.VictoryCondition;
import samuel.game.GameContext;
import samuel.introductory.IntroductoryVictoryCondition;
import samuel.player.Player;
import samuel.point.points.VictoryPoint;

import java.util.List;
import java.util.concurrent.locks.Condition;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IntroductoryVictoryConditionTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    private VictoryCondition condition;

    @BeforeEach
    void setup() {
//        when(context.getPlayers()).thenReturn(List.of(player));
        condition = new IntroductoryVictoryCondition();
    }

    @Test
    void testMoreThan7() {
        when(context.getVictoryPoints(player)).thenReturn(8);

        assertTrue(condition.hasWon(player, context));
    }

    @Test
    void testEqual7() {
        when(context.getVictoryPoints(player)).thenReturn(7);

        assertTrue(condition.hasWon(player, context));
    }


    @Test
    void testLessThan7() {
        when(context.getVictoryPoints(player)).thenReturn(6);

        assertFalse(condition.hasWon(player, context));
    }
}
