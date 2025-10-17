package samuel.test.common.card.ship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.board.GridBoard;
import samuel.card.ExpansionCard;
import samuel.card.building.BuildingCard;
import samuel.card.region.ForestRegionCard;
import samuel.card.region.PastureRegionCard;
import samuel.card.ship.GrainShipCard;
import samuel.card.ship.LargeTradeShipCard;
import samuel.card.ship.TradeShipCard;
import samuel.event.GenericEventBus;
import samuel.event.player.PlayerTradeEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.network.SocketClient;
import samuel.player.Player;
import samuel.player.PlayerHand;
import samuel.player.ServerPlayer;
import samuel.point.points.CommercePoint;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.LumberResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LargeTradeShipTest {
    @Mock
    private SocketClient client;

    @Mock
    private PlayerHand hand;

    @Mock
    private GameContext context;

    private Player player;
    private Board board;
    private EventBus eventBus;

    private ExpansionCard ship;

    private final int commercePoints = 1;
    private final Class<? extends Resource> toTrade = LumberResource.class;

    private BoardPosition position;

    @BeforeEach
    void setup() {
        board = GridBoard.createGridBoard(5,7);
        position = board.getPositionFromGrid(1,2);
        player = new ServerPlayer(board, hand, client);
        eventBus = new GenericEventBus();
        when(context.getEventBus()).thenReturn(eventBus);
        ship = new LargeTradeShipCard();
    }

    @Test
    void testPoints() {
        board.place(ship, position);
        ship.onPlace(player, context, position);

        assertEquals(commercePoints, player.getPoints(CommercePoint.class));
    }

    @Test
    void testFunctionality() {
        board.place(ship, position);
        ship.onPlace(player, context, position);
        board.place(new ForestRegionCard(1), board.getPositionFromGrid(1, 1));
        board.place(new PastureRegionCard(1), board.getPositionFromGrid(1, 3));

        PlayerTradeEvent event = new PlayerTradeEvent(player, new ResourceAmount(toTrade, 3), new ResourceAmount(GoldResource.class, 1));
        eventBus.fireEvent(event);

        assertEquals(2, event.getResourceToPay().amount());
    }

    @Test
    void testWrongResource() {
        board.place(ship, board.getPositionFromGrid(1,1));
        ship.onPlace(player, context, board.getPositionFromGrid(1,1));
        board.place(new ForestRegionCard(1), board.getPositionFromGrid(1, 1));
        board.place(new PastureRegionCard(1), board.getPositionFromGrid(1, 3));

        PlayerTradeEvent event = new PlayerTradeEvent(player, new ResourceAmount(GoldResource.class, 3), new ResourceAmount(GoldResource.class, 1));
        eventBus.fireEvent(event);

        assertEquals(3, event.getResourceToPay().amount());
    }
}
