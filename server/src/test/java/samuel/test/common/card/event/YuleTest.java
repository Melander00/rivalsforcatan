package samuel.test.common.card.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.event.*;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.stack.EventCardStack;
import samuel.stack.GenericStackContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class YuleTest {
    @Mock
    private GameContext context;

    @Mock
    private StackContainer container;

    @Mock
    private EventCard eventCard;

    private CardStack<EventCard> cardStack;
    private EventCard card;
    private EventBus eventBus;

    @BeforeEach
    void setup() {
        List<EventCard> eventCards = List.of(
                eventCard, // 0
                eventCard, // 1
                eventCard, // 2
                eventCard, // 3
                eventCard, // 4
                eventCard, // 5
                eventCard, // 6
                new YuleEventCard() // 7
        );

        card = new YuleEventCard();
        cardStack = new EventCardStack();
        for(EventCard c : eventCards) {
            cardStack.addCardToBottom(c);
        }
        eventBus = new GenericEventBus();

        when(container.getEventStack()).thenReturn(cardStack);
        when(context.getEventBus()).thenReturn(eventBus);
        when(context.getStackContainer()).thenReturn(container);
    }

    @Test
    void testYuleShuffle() {
        card.resolveEvent(context);

        /*
        The shuffle means that the fourth from bottom is Yule
        But we also resolve a new event and places that card in the bottom
        Which means that after it is resolved we should have Yule at fifth from bottom.
         */

        assertEquals(card.getCardID(), cardStack.getCards().get(3).getCardID()); // <- Make sure fourth from bottom is a Yule Card.
        verify(eventCard, times(1)).resolveEvent(context);
    }
}
