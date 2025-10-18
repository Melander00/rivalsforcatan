package samuel.test.common.card.action;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.card.action.ActionCard;
import samuel.card.action.ScoutActionCard;
import samuel.card.stack.StackContainer;
import samuel.event.GenericEventBus;
import samuel.event.player.PlayerTakeRegionCardsEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ScoutTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    @Mock
    private StackContainer stackContainer;

    private EventBus eventBus;
    private ActionCard card;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        card = new ScoutActionCard();
    }

    @Test
    @Disabled
    void testFunctionality() {
        when(context.getStackContainer()).thenReturn(stackContainer);

        card.onPlay(player, context);
        PlayerTakeRegionCardsEvent event = new PlayerTakeRegionCardsEvent();
        context.getEventBus().fireEvent(event);


        assertTrue(event.isCanceled());

        // todo
    }

    @Test
    void testCanPlay() {
        when(context.getPhase()).thenReturn(Phase.ACTION);

        assertTrue(card.canPlay(player, context));
    }

    @Test
    void testCantPlay() {
        when(context.getPhase()).thenReturn(Phase.DICE_ROLL);

        assertFalse(card.canPlay(player, context));
    }

}
