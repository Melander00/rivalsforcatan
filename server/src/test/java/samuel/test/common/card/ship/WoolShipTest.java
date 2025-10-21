package samuel.test.common.card.ship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.center.SettlementCard;
import samuel.card.ship.SingleResourceTradeShipCard;
import samuel.card.ship.WoolShipCard;
import samuel.event.GenericEventBus;
import samuel.event.player.PlayerTradeEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.network.SocketClient;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.PlayerHand;
import samuel.player.ServerPlayer;
import samuel.point.points.CommercePoint;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.LumberResource;
import samuel.resource.resources.WoolResource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class WoolShipTest {
    @Mock
    private SocketClient client;

    @Mock
    private PlayerHand hand;

    @Mock
    private GameContext context;

    private Player player;
    private Board board;
    private EventBus eventBus;

    private SingleResourceTradeShipCard ship;

    private final int commercePoints = 1;
    private final Class<? extends Resource> toTrade = WoolResource.class;

    @BeforeEach
    void setup() {
        board = GridBoard.createGridBoard(5,7);
        player = new ServerPlayer(board, hand, null);
        eventBus = new GenericEventBus();
        ship = new WoolShipCard();
    }

    @Test
    void testPoints() {
        when(context.getEventBus()).thenReturn(eventBus);

        board.place(ship, board.getPositionFromGrid(1,1));
        ship.onPlace(player, context, board.getPositionFromGrid(1,1));

        assertEquals(commercePoints, player.getPoints(CommercePoint.class));
    }

    @Test
    void testFunctionality() {
        when(context.getEventBus()).thenReturn(eventBus);

        board.place(ship, board.getPositionFromGrid(1,1));
        ship.onPlace(player, context, board.getPositionFromGrid(1,1));
        PlayerTradeEvent event = new PlayerTradeEvent(player, new ResourceAmount(toTrade, 3), new ResourceAmount(GoldResource.class, 1));
        eventBus.fireEvent(event);

        assertEquals(2, event.getResourceToPay().amount());
    }

    @Test
    void testWrongResource() {
        when(context.getEventBus()).thenReturn(eventBus);

        board.place(ship, board.getPositionFromGrid(1,1));
        ship.onPlace(player, context, board.getPositionFromGrid(1,1));
        PlayerTradeEvent event = new PlayerTradeEvent(player, new ResourceAmount(GoldResource.class, 3), new ResourceAmount(GoldResource.class, 1));
        eventBus.fireEvent(event);

        assertEquals(3, event.getResourceToPay().amount());
    }

    @Test
    void testCorrectResources() {
        assertEquals(createBundle(LumberResource.class, WoolResource.class), ship.getCost());
    }

    @Test
    void testCanPlay() {
        Player testPlayer = mock(Player.class);
        when(context.getPhase()).thenReturn(Phase.ACTION);
        when(testPlayer.hasResources(any())).thenReturn(true);

        assertTrue(ship.canPlay(testPlayer, context));
    }

    @Test
    void testCantPlay() {
        when(context.getPhase()).thenReturn(Phase.DICE_ROLL);

        assertFalse(ship.canPlay(player, context));
    }

    @Test
    void testCorrectPlacement() {
        board.place(new SettlementCard(), board.getPositionFromGrid(2,2));

        assertTrue(ship.validatePlacement(board.getPositionFromGrid(1,2)));
    }

    @Test
    void testIncorrectPlacement() {
        assertFalse(ship.validatePlacement(board.getPositionFromGrid(1,2)));
    }
}
