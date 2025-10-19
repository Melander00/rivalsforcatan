package samuel.test.common.card.building;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.board.GridBoard;
import samuel.card.building.AbbeyBuildingCard;
import samuel.card.building.BuildingCard;
import samuel.card.building.ParishHallBuildingCard;
import samuel.card.center.SettlementCard;
import samuel.event.GenericEventBus;
import samuel.event.player.exchange.PlayerExchangeSearchEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.network.SocketClient;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.PlayerHand;
import samuel.player.ServerPlayer;
import samuel.point.points.ProgressPoint;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.OreResource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class ParishHallTest {


    @Mock
    private SocketClient client;

    @Mock
    private PlayerHand hand;

    @Mock
    private GameContext context;

    private Player player;
    private Board board;
    private EventBus eventBus;

    private BuildingCard card;

    @BeforeEach
    void setup() {
        board = GridBoard.createGridBoard(5,7);
        player = new ServerPlayer(board, hand, client);
        eventBus = new GenericEventBus();
        card = new ParishHallBuildingCard();
    }

    @Test
    void testExchangeSearch() {
        when(context.getEventBus()).thenReturn(eventBus);
        // place parish hall
        BoardPosition pos = board.getPositionFromGrid(1,1);
        board.place(card, pos);
        card.onPlace(player, context, pos);

        // fire exchange event
        PlayerExchangeSearchEvent event = new PlayerExchangeSearchEvent(player, 2);
        eventBus.fireEvent(event);

        // make sure only one resource was taken.
        assertEquals(1, event.getResourcesToPay());
    }


    @Test
    void testCorrectResources() {
        assertEquals(createBundle(BrickResource.class, GrainResource.class), card.getCost());
    }

    @Test
    void testCanPlay() {
        Player testPlayer = mock(Player.class);
        when(context.getPhase()).thenReturn(Phase.ACTION);
        when(testPlayer.getPrincipality()).thenReturn(board);
        when(testPlayer.hasResources(any())).thenReturn(true);

        assertTrue(card.canPlay(testPlayer, context));
    }

    @Test
    void testCantPlay() {
        when(context.getPhase()).thenReturn(Phase.DICE_ROLL);

        assertFalse(card.canPlay(player, context));
    }

    @Test
    void testCorrectPlacement() {
        board.place(new SettlementCard(), board.getPositionFromGrid(2,2));

        assertTrue(card.validatePlacement(board.getPositionFromGrid(1,2)));
    }

    @Test
    void testIncorrectPlacement() {
        assertFalse(card.validatePlacement(board.getPositionFromGrid(1,2)));
    }

    @Test
    void testUniqueness() {
        when(context.getPhase()).thenReturn(Phase.ACTION);
        Player testPlayer = mock(Player.class);
        when(testPlayer.hasResources(any())).thenReturn(true);
        when(testPlayer.getPrincipality()).thenReturn(board);
        BuildingCard card2 = new ParishHallBuildingCard();

        board.place(card2, board.getPositionFromGrid(1,1));

        assertFalse(card.canPlay(testPlayer, context));
    }

}
