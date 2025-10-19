package samuel.test.server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.die.EventDieFace;
import samuel.die.face.BrigandAttackFace;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.EventBus;
import samuel.game.DefaultDiceHandler;
import samuel.game.DiceHandler;
import samuel.game.GameContext;
import samuel.player.Player;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiceTest {


    @Mock
    private GameContext context;

    @Mock
    private Player player;

    private DiceHandler handler;

    @BeforeEach
    void setup() {
        handler = new DefaultDiceHandler();
    }

    @Test
    void testProductionDice() {
        EventBus eventBus = mock(EventBus.class);

        when(context.getEventBus()).thenReturn(eventBus);
        when(context.rollProductionDie()).thenReturn(1);

        handler.rollProductionDice(player, context);

        verify(eventBus, times(1)).fireEvent(any());
    }

    @Test
    void testEventDice() {
        EventBus eventBus = mock(EventBus.class);

        when(context.getEventBus()).thenReturn(eventBus);
        when(context.rollEventDice()).thenReturn(mock(EventDieFace.class));

        handler.rollEventDice(player, context);

        verify(eventBus, times(1)).fireEvent(any());
    }

    @Test
    void testEventBeforeProduction() {
        EventBus eventBus = mock(EventBus.class);
        EventDieFace face = mock(BrigandAttackFace.class);

        when(context.getEventBus()).thenReturn(eventBus);
        when(face.hasPriorityOverProduction()).thenReturn(true);
        when(context.rollEventDice()).thenReturn(face);

        handler.rollAndResolveDice(player, context);

        InOrder order = inOrder(eventBus, face);

        order.verify(face, times(1)).resolve(context);
        order.verify(eventBus, times(1)).fireEvent(isA(ProductionDieEvent.Post.class));
    }

    @Test
    void testEventAfterProduction() {
        EventBus eventBus = mock(EventBus.class);
        EventDieFace face = mock(EventDieFace.class);

        when(context.getEventBus()).thenReturn(eventBus);
        when(face.hasPriorityOverProduction()).thenReturn(false);
        when(context.rollProductionDie()).thenReturn(1);
        when(context.rollEventDice()).thenReturn(face);

        handler.rollAndResolveDice(player, context);

        InOrder order = inOrder(eventBus, face);

        order.verify(eventBus, times(1)).fireEvent(isA(ProductionDieEvent.Post.class));
        order.verify(face, times(1)).resolve(context);
    }

}
