package samuel.test.common.card.ship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.hero.AustinHeroCard;
import samuel.card.hero.HeroCard;
import samuel.card.ship.OreShipCard;
import samuel.card.ship.TradeShipCard;
import samuel.event.GenericEventBus;
import samuel.event.player.PlayerTradeEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.network.SocketClient;
import samuel.player.Player;
import samuel.player.PlayerHand;
import samuel.player.ServerPlayer;
import samuel.point.PointBundle;
import samuel.point.points.CommercePoint;
import samuel.point.points.SkillPoint;
import samuel.point.points.StrengthPoint;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.OreResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OreShipTest {
    @Mock
    private SocketClient client;

    @Mock
    private PlayerHand hand;

    @Mock
    private GameContext context;

    private Player player;
    private Board board;
    private EventBus eventBus;

    private TradeShipCard ship;

    private final int commercePoints = 1;
    private final Class<? extends Resource> toTrade = OreResource.class;

    @BeforeEach
    void setup() {
        board = GridBoard.createGridBoard(5,7);
        player = new ServerPlayer(board, hand, client);
        eventBus = new GenericEventBus();
        when(context.getEventBus()).thenReturn(eventBus);
        ship = new OreShipCard();
    }

    @Test
    void testPoints() {
        board.place(ship, board.getPositionFromGrid(1,1));
        ship.onPlace(player, context, board.getPositionFromGrid(1,1));

        assertEquals(commercePoints, player.getPoints(CommercePoint.class));
    }

    @Test
    void testFunctionality() {
        board.place(ship, board.getPositionFromGrid(1,1));
        ship.onPlace(player, context, board.getPositionFromGrid(1,1));
        PlayerTradeEvent event = new PlayerTradeEvent(player, new ResourceAmount(toTrade, 3), new ResourceAmount(GoldResource.class, 1));
        eventBus.fireEvent(event);

        assertEquals(2, event.getResourceToPay().amount());
    }

    @Test
    void testWrongResource() {
        board.place(ship, board.getPositionFromGrid(1,1));
        ship.onPlace(player, context, board.getPositionFromGrid(1,1));
        PlayerTradeEvent event = new PlayerTradeEvent(player, new ResourceAmount(GoldResource.class, 3), new ResourceAmount(GoldResource.class, 1));
        eventBus.fireEvent(event);

        assertEquals(3, event.getResourceToPay().amount());
    }


}
