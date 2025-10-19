package samuel.test.server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.card.PlayableCard;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.DefaultReplenishHandler;
import samuel.game.GameContext;
import samuel.game.ReplenishHandler;
import samuel.player.Player;
import samuel.player.PlayerHand;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReplenishTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    @Mock
    private PlayerHand hand;

    private EventBus eventBus;
    private ReplenishHandler handler;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        handler = new DefaultReplenishHandler();
    }

    @Test
    void testHandFull() {
        when(player.isHandFull()).thenReturn(true);
        when(player.getHand()).thenReturn(hand);
        when(player.getMaxHandSize()).thenReturn(3);
        when(hand.getSize()).thenReturn(3);

        handler.handleReplenish(player, context);

        verify(player, times(0)).addCardToHand(any());
    }

    @Test
    void testHandOverfull() {
        when(player.isHandFull()).thenReturn(true);
        when(player.getHand()).thenReturn(hand);
        when(player.getMaxHandSize()).thenReturn(3);
        when(hand.getSize()).thenReturn(4);
        when(player.requestCard(any(),any())).thenReturn(mock(PlayableCard.class));

        handler.handleReplenish(player, context);

        verify(player, times(1)).removeCardFromHand(any());
        verify(player, times(0)).addCardToHand(any());
    }

    @Test
    void testHandMissingOne() {

    }

    @Test
    void testHandMissingMultiple() {

    }

}
