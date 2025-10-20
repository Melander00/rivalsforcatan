package samuel.test.common.die;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.die.EventDieFace;
import samuel.die.face.TradeFace;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.GoldResource;

import java.util.List;

import static org.mockito.Mockito.*;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class TradeFaceTest {

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
        face = new TradeFace();
        eventBus = new GenericEventBus();
        when(context.getEventBus()).thenReturn(eventBus);
    }

    @Test
    void testNoAdvantage() {
        when(context.getTradeAdvantage()).thenReturn(null);

        face.resolve(context);

        verify(player,times(0)).giveResources(any());
        verify(opponent,times(0)).removeResources(any());
    }

    @Test
    void testNoResources() {
        when(context.getTradeAdvantage()).thenReturn(player);
        when(context.getPlayers()).thenReturn(List.of(player, opponent));
        when(opponent.getResources()).thenReturn(new ResourceBundle());

        face.resolve(context);

        verify(player,times(0)).giveResources(any());
        verify(opponent,times(0)).removeResources(any());
    }

    @Test
    void testAdvantage() {
        when(context.getTradeAdvantage()).thenReturn(player);
        when(context.getPlayers()).thenReturn(List.of(player, opponent));
        ResourceBundle oppResources = createBundle(GoldResource.class);
        when(opponent.getResources()).thenReturn(oppResources);
        ResourceBundle toGet = createBundle(GoldResource.class);
        when(player.requestResource(eq(oppResources), eq(1), any())).thenReturn(toGet);

        face.resolve(context);

        verify(player,times(1)).giveResources(toGet);
        verify(opponent,times(1)).removeResources(toGet);
    }

}
