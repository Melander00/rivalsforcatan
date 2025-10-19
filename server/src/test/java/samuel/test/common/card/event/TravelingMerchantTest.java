package samuel.test.common.card.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.event.EventCard;
import samuel.card.event.TravelingMerchantEventCard;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.LumberResource;
import samuel.resource.resources.OreResource;

import java.util.List;

import static org.mockito.Mockito.*;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class TravelingMerchantTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    private Board board;
    private EventCard card;
    private EventBus eventBus;

    @BeforeEach
    void setup() {
        board = GridBoard.createGridBoard(5,7);
        card = new TravelingMerchantEventCard();
        eventBus = new GenericEventBus();

        when(context.getEventBus()).thenReturn(eventBus);
        when(context.getPlayers()).thenReturn(List.of(player));
    }

    @Test
    void testNoGold() {
        when(player.getResources(GoldResource.class)).thenReturn(0);

        card.resolveEvent(context);

        verify(player, times(0)).giveResources(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2})
    void testFunctionality(int amount) {
        when(player.getResources(GoldResource.class)).thenReturn(amount);
        when(player.requestInt(eq(0), eq(amount), any())).thenReturn(amount);
        ResourceBundle toGet = new ResourceBundle();
        when(player.requestResource(any(), eq(amount), any())).thenReturn(toGet);

        card.resolveEvent(context);

        verify(player, times(1)).giveResources(toGet);
        verify(player, times(1)).removeResources(ResourceBundle.fromAmount(new ResourceAmount(GoldResource.class, amount)));
    }

    @Test
    void testMoreGold() {

        when(player.getResources(GoldResource.class)).thenReturn(4);
        when(player.requestInt(eq(0), eq(2), any())).thenReturn(1);
        ResourceBundle toGet = new ResourceBundle();
        when(player.requestResource(any(), eq(1), any())).thenReturn(toGet);

        card.resolveEvent(context);

        verify(player, times(1)).giveResources(toGet);
        verify(player, times(1)).removeResources(ResourceBundle.fromAmount(new ResourceAmount(GoldResource.class, 1)));
    }
}
