package samuel.test.common.die;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.die.EventDieFace;
import samuel.die.face.CelebrationFace;
import samuel.die.face.TradeFace;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.point.points.ProgressPoint;
import samuel.point.points.SkillPoint;
import samuel.resource.ResourceBundle;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CelebrationTest {
    @Mock
    private Player player;

    @Mock
    private Player opponent;

    @Mock
    private GameContext context;

    private EventDieFace face;
    private EventBus eventBus;

    @BeforeEach
    void setup() {
        face = new CelebrationFace();
        eventBus = new GenericEventBus();
        when(context.getEventBus()).thenReturn(eventBus);
        when(context.getPlayers()).thenReturn(List.of(player, opponent));
    }

    @Test
    void testNoSkillPoints() {
        when(player.getPoints(SkillPoint.class)).thenReturn(0);
        when(opponent.getPoints(SkillPoint.class)).thenReturn(0);
        when(player.requestResource(any(),anyInt(),any())).thenReturn(new ResourceBundle());
        when(opponent.requestResource(any(),anyInt(),any())).thenReturn(new ResourceBundle());

        face.resolve(context);

        verify(player, times(1)).giveResources(any());
        verify(opponent, times(1)).giveResources(any());
    }

    @Test
    void testEqualSkillPoints() {
        when(player.getPoints(SkillPoint.class)).thenReturn(1);
        when(opponent.getPoints(SkillPoint.class)).thenReturn(1);
        when(player.requestResource(any(),anyInt(),any())).thenReturn(new ResourceBundle());
        when(opponent.requestResource(any(),anyInt(),any())).thenReturn(new ResourceBundle());

        face.resolve(context);

        verify(player, times(1)).giveResources(any());
        verify(opponent, times(1)).giveResources(any());

    }

    @Test
    void testPlayerMostSkillPoints() {
        when(player.getPoints(SkillPoint.class)).thenReturn(2);
        when(opponent.getPoints(SkillPoint.class)).thenReturn(1);
        when(player.requestResource(any(),anyInt(),any())).thenReturn(new ResourceBundle());
//        when(opponent.requestResource(any(),any(),any())).thenReturn(new ResourceBundle());

        face.resolve(context);

        verify(player, times(1)).giveResources(any());
        verify(opponent, times(0)).giveResources(any());

    }
}
