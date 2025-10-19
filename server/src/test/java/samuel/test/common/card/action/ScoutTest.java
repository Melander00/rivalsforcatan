package samuel.test.common.card.action;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.action.ActionCard;
import samuel.card.action.ScoutActionCard;
import samuel.card.center.SettlementCard;
import samuel.card.region.*;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.event.GenericEventBus;
import samuel.event.player.PlayerTakeRegionCardsEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.stack.GenericCardStack;
import samuel.test.common.card.building.booster.GrainMillTest;

import java.util.List;

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

    private CardStack<RegionCard> stack;

    private EventBus eventBus;
    private Board board;
    private ActionCard card;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        card = new ScoutActionCard();
        board = GridBoard.createGridBoard(5,7);
        stack = new GenericCardStack<>();

    }

    @Test
    void testFunctionality() {
        when(context.getEventBus()).thenReturn(eventBus);
        when(context.getStackContainer()).thenReturn(stackContainer);
        when(stackContainer.getRegionStack()).thenReturn(stack);

        RegionCard r1 = new ForestRegionCard(1);
        RegionCard r2 = new GoldFieldRegionCard(1);
        RegionCard r3 = new FieldsRegionCard(1);
        RegionCard r4 = new MountainsRegionCard(1);

        stack.addCardToBottom(r1);
        stack.addCardToBottom(r2);
        stack.addCardToBottom(r3);
        stack.addCardToBottom(r4);

        when(player.requestCard(anyList(), any()))
                .thenReturn(r3)
                .thenReturn(r4);

        card.onPlay(player, context);
        (new SettlementCard()).onPlace(player, context, board.getPositionFromGrid(1,1));

        verify(player, times(0)).playCard(r1, context);
        verify(player, times(0)).playCard(r2, context);
        verify(player, times(1)).playCard(r3, context);
        verify(player, times(1)).playCard(r4, context);
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
