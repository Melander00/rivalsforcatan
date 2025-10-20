package samuel.test.common.die;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.die.EventDieFace;
import samuel.die.face.CelebrationFace;
import samuel.die.face.PlentifulHarvestFace;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.ResourceBundle;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlentifulHarvestTest {
    @Mock
    private Player player;

    @Mock
    private GameContext context;

    private EventDieFace face;
    private EventBus eventBus;

    @BeforeEach
    void setup() {
        face = new PlentifulHarvestFace();
        eventBus = new GenericEventBus();
        when(context.getEventBus()).thenReturn(eventBus);
        when(context.getPlayers()).thenReturn(List.of(player));
    }

    @Test
    void testFunctionality() {
        ResourceBundle toGet = new ResourceBundle();
        when(player.requestResource(any(), eq(1), any())).thenReturn(toGet);

        face.resolve(context);

        verify(player, times(1)).giveResources(toGet);
    }
}
