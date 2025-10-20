package samuel.test.common.die;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.card.event.EventCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.die.EventDieFace;
import samuel.die.face.EventCardFace;
import samuel.die.face.PlentifulHarvestFace;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventCardTest {

    @Mock
    private GameContext context;

    @Mock
    private StackContainer stackContainer;

    @Mock
    private CardStack<EventCard> stack;

    @Mock
    private EventCard card;

    private EventDieFace face;
    private EventBus eventBus;

    @BeforeEach
    void setup() {
        face = new EventCardFace();
        eventBus = new GenericEventBus();
        when(context.getEventBus()).thenReturn(eventBus);
        when(context.getStackContainer()).thenReturn(stackContainer);
        when(stackContainer.getEventStack()).thenReturn(stack);
        when(stack.takeTopCard()).thenReturn(card);
    }

    @Test
    void testFunctionality() {
        InOrder order = inOrder(stack, card);

        face.resolve(context);

        order.verify(stack, times(1)).takeTopCard();
        order.verify(stack, times(1)).addCardToBottom(card);
        order.verify(card, times(1)).resolveEvent(context);
    }
}
