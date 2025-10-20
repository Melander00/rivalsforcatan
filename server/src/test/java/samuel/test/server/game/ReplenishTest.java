package samuel.test.server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.card.PlayableCard;
import samuel.card.action.BrigittaTheWiseWomanActionCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.DefaultReplenishHandler;
import samuel.game.GameContext;
import samuel.game.ReplenishHandler;
import samuel.player.GenericPlayerHand;
import samuel.player.Player;
import samuel.player.PlayerHand;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReplenishTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    @Mock
    private PlayerHand hand;

    @Mock
    private CardStack<PlayableCard> stack;

    @Mock
    private StackContainer container;

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
        PlayerHand hand = new GenericPlayerHand();
        PlayableCard toRemove = mock(PlayableCard.class);

        hand.addCard(toRemove);
        hand.addCard(mock(PlayableCard.class));
        hand.addCard(mock(PlayableCard.class));
        hand.addCard(mock(PlayableCard.class));

        when(player.isHandFull()).thenReturn(true);
        when(player.getHand()).thenReturn(hand);
        when(player.getMaxHandSize()).thenReturn(3);
        when(context.getStackContainer()).thenReturn(container);
        when(container.getBasicStacks()).thenReturn(List.of(stack));
        when(player.requestCard(any(),any())).thenReturn(toRemove);
        when(player.requestCardStack(any(), any(), any())).thenReturn(stack);
        when(context.getEventBus()).thenReturn(eventBus);
        doAnswer(i -> {
            PlayableCard c = i.getArgument(0);
            hand.removeCard(c);
            return null;
        }).when(player).removeCardFromHand(any());

        handler.handleReplenish(player, context);

        verify(player, times(1)).removeCardFromHand(toRemove);
        verify(player, times(0)).addCardToHand(any());
    }

    @Test
    void testHandMissingOne() {
        PlayerHand hand = new GenericPlayerHand();
        hand.addCard(new BrigittaTheWiseWomanActionCard());
        hand.addCard(new BrigittaTheWiseWomanActionCard());

        PlayableCard card = mock(PlayableCard.class);

        when(player.isHandFull()).then((i) -> hand.getSize() >= 3);
        when(player.requestCardStack(any(), any(), any())).thenReturn(stack);
        when(stack.getSize()).thenReturn(10);
        when(stack.takeTopCard()).thenReturn(card);
        when(context.getStackContainer()).thenReturn(container);
        when(container.getBasicStacks()).thenReturn(List.of(stack));
        when(context.getEventBus()).thenReturn(eventBus);
        doAnswer(i -> {
            PlayableCard c = i.getArgument(0);
            hand.addCard(c);
            return null;
        }).when(player).addCardToHand(any());

        handler.handleReplenish(player, context);

        verify(player, times(1)).addCardToHand(card);

    }

    @Test
    void testHandMissingMultiple() {
        PlayerHand hand = new GenericPlayerHand();

        when(player.isHandFull()).then((i) -> hand.getSize() >= 3);
        when(player.requestCardStack(any(), any(), any())).thenReturn(stack);
        when(stack.getSize()).thenReturn(10);
        when(stack.takeTopCard()).then((i) -> mock(PlayableCard.class));
        when(context.getStackContainer()).thenReturn(container);
        when(container.getBasicStacks()).thenReturn(List.of(stack));
        when(context.getEventBus()).thenReturn(eventBus);
        doAnswer(i -> {
            PlayableCard c = i.getArgument(0);
            hand.addCard(c);
            return null;
        }).when(player).addCardToHand(any());

        handler.handleReplenish(player, context);

        verify(player, times(3)).addCardToHand(any());
    }

}
