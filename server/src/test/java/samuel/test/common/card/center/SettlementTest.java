package samuel.test.common.card.center;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.center.RoadCard;
import samuel.card.center.SettlementCard;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.resource.resources.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class SettlementTest {
    @Mock
    private GameContext context;

    @Mock
    private Player player;

    private Board board;

    private SettlementCard card;

    @BeforeEach
    void setup() {
        board = GridBoard.createGridBoard(5, 7);
        card = new SettlementCard();
    }

    @Test
    void testCorrectResources() {
        assertEquals(createBundle(
                LumberResource.class, BrickResource.class, GrainResource.class, WoolResource.class
        ), card.getCost());
    }

    @Test
    void testCanPlay() {
        when(context.getPhase()).thenReturn(Phase.ACTION);
        when(player.hasResources(any())).thenReturn(true);

        assertTrue(card.canPlay(player , context));
    }

    @Test
    void testCantPlay() {
        when(context.getPhase()).thenReturn(Phase.DICE_ROLL);

        assertFalse(card.canPlay(player, context));
    }

    @Test
    void testCorrectPlacement() {
        board.place(new RoadCard(), board.getPositionFromGrid(2,2));

        assertTrue(card.validatePlacement(board.getPositionFromGrid(2,1)));
    }

    @Test
    void testIncorrectPlacement() {
        assertFalse(card.validatePlacement(board.getPositionFromGrid(2,1)));
    }
}
