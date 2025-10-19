package samuel.test.server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.eventmanager.Subscribe;
import samuel.game.DefaultExchangeHandler;
import samuel.game.ExchangeHandler;
import samuel.game.GameContext;
import samuel.player.GenericPlayerHand;
import samuel.player.Player;
import samuel.player.PlayerHand;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.LumberResource;
import samuel.stack.GenericCardStack;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class ExchangeTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    @Mock
    private StackContainer stackContainer;

    private CardStack<PlayableCard> stack;
    private PlayerHand hand;
    private ExchangeHandler handler;
    private EventBus eventBus;

    @BeforeEach
    void setup() {
        stack = new GenericCardStack<>();
        hand = new GenericPlayerHand();
        handler = new DefaultExchangeHandler();
        eventBus = new GenericEventBus();
    }

    @Test
    void testNoExchange() {
        when(player.requestBoolean(any())).thenReturn(false);

        handler.handleExchange(player, context);

        verify(player, times(0)).addCardToHand(any());
    }

    @Test
    void testNoSearch() {
        when(player.getHand()).thenReturn(hand);
        when(context.getStackContainer()).thenReturn(stackContainer);
        when(context.getEventBus()).thenReturn(eventBus);
        when(stackContainer.getBasicStacks()).thenReturn(List.of(stack));

        when(player.requestBoolean(any())).thenReturn(true).thenReturn(false);
        PlayableCard inHand = mock(PlayableCard.class);
        hand.addCard(inHand);
        PlayableCard inStack = mock(PlayableCard.class);
        stack.addCardToBottom(inStack);
        when(player.requestCard(any(), any())).thenReturn(inHand);
        when(player.requestCardStack(any(), any(), any())).thenReturn(stack);

        handler.handleExchange(player, context);

        verify(player, times(1)).addCardToHand(inStack);
        assertEquals(inHand, stack.getCards().getFirst());
    }

    @Test
    void testSearch() {
        when(player.getHand()).thenReturn(hand);
        when(context.getStackContainer()).thenReturn(stackContainer);
        when(context.getEventBus()).thenReturn(eventBus);
        when(stackContainer.getBasicStacks()).thenReturn(List.of(stack));

        when(player.requestBoolean(any())).thenReturn(true).thenReturn(true);
        PlayableCard inHand = mock(PlayableCard.class);
        hand.addCard(inHand);
        PlayableCard inStack = mock(PlayableCard.class);
        stack.addCardToBottom(mock(PlayableCard.class));
        stack.addCardToBottom(inStack);
        stack.addCardToBottom(mock(PlayableCard.class));
        when(player.requestCard(any(), any())).thenReturn(inHand).thenReturn(inStack);
        when(player.requestCardStack(any(), any(), any())).thenReturn(stack);
        when(player.requestResource(any(),anyInt(),any())).thenReturn(createBundle(LumberResource.class, GoldResource.class));

        handler.handleExchange(player, context);

        verify(player, times(1)).addCardToHand(inStack);
        assertTrue(stack.getCards().contains(inHand));
    }
}
