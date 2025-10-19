package samuel.test.common.card.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.PlayableCard;
import samuel.card.event.EventCard;
import samuel.card.event.FeudEventCard;
import samuel.card.event.FraternalFeudsEventCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.GenericPlayerHand;
import samuel.player.Player;
import samuel.player.PlayerHand;
import samuel.stack.GenericCardStack;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FraternalFeudsTest {


    @Mock
    private Player p1;

    @Mock
    private Player p2;

    private PlayerHand h2;

    @Mock
    private GameContext context;

    @Mock
    private StackContainer stackContainer;

    @Mock
    private CardStack<PlayableCard> stack;

    private EventBus eventBus;
    private EventCard card;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        card = new FraternalFeudsEventCard();
        h2 = new GenericPlayerHand();

        List<PlayableCard> cardsInHand = List.of(
                mock(PlayableCard.class),
                mock(PlayableCard.class),
                mock(PlayableCard.class)
        );

        when(context.getEventBus()).thenReturn(eventBus);
    }

    @Test
    void testNoPlayerAdvantage() {
        when(context.getStrengthAdvantage()).thenReturn(null);

        card.resolveEvent(context);

        verify(p1, times(0)).removeCardFromHand(any());
        verify(p2, times(0)).removeCardFromHand(any());
    }

    @Test
    void testFunctionality() {
        when(context.getStrengthAdvantage()).thenReturn(p1);
        when(context.getStackContainer()).thenReturn(stackContainer);
        when(context.getPlayers()).thenReturn(List.of(p1, p2));
        List<CardStack<PlayableCard>> stacks = List.of(stack);
        when(stackContainer.getBasicStacks()).thenReturn(stacks);

        when(p2.getHand()).thenReturn(h2);
        PlayableCard pc1 = mock(PlayableCard.class);
        PlayableCard pc2 = mock(PlayableCard.class);
        List<PlayableCard> cardsInHand = List.of(
                pc1,
                pc2,
                mock(PlayableCard.class)
        );
        for(PlayableCard c : cardsInHand) {
            h2.addCard(c);
        }
        when(p1.requestCard(any(), any())).thenReturn(pc1).thenReturn(pc2);
        when(p1.requestCardStack(eq(stacks), any(), any())).thenReturn(stack);

        card.resolveEvent(context);

        verify(p2, times(1)).removeCardFromHand(pc1);
        verify(stack, times(1)).addCardToBottom(pc1);
        verify(p2, times(1)).removeCardFromHand(pc2);
        verify(stack, times(1)).addCardToBottom(pc2);
    }


}
