package samuel.test.common.card.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.event.EventCard;
import samuel.card.event.TradeShipsRaceEventCard;
import samuel.card.event.YearOfPlentyEventCard;
import samuel.card.ship.GoldShipCard;
import samuel.card.ship.LargeTradeShipCard;
import samuel.card.ship.OreShipCard;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.LumberResource;

import java.util.List;

import static org.mockito.Mockito.*;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class TradeShipsRaceTest {



    @Mock
    private Player player;

    @Mock
    private Player opponent;

    @Mock
    private GameContext context;

    private Board board;
    private Board opponentBoard;
    private EventCard card;
    private EventBus eventBus;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        board = GridBoard.createGridBoard(5,7);
        opponentBoard = GridBoard.createGridBoard(5,7);
        card = new TradeShipsRaceEventCard();

        when(player.getPrincipality()).thenReturn(board);
        when(opponent.getPrincipality()).thenReturn(opponentBoard);
        when(context.getPlayers()).thenReturn(List.of(player, opponent));
        when(context.getEventBus()).thenReturn(eventBus);
    }

    @Test
    void testNoShips() {
        card.resolveEvent(context);

        verify(player, times(0)).requestResource(any(),anyInt(),any());
        verify(opponent, times(0)).requestResource(any(),anyInt(),any());
    }

    @Test
    void testTie() {
        board.place(new LargeTradeShipCard(), board.getPositionFromGrid(1,1));
        opponentBoard.place(new OreShipCard(), opponentBoard.getPositionFromGrid(1,1));

        ResourceBundle p1 = createBundle(LumberResource.class);
        when(player.requestResource(any(), eq(1), any())).thenReturn(p1);

        ResourceBundle p2 = createBundle(GoldResource.class);
        when(opponent.requestResource(any(), eq(1), any())).thenReturn(p2);

        card.resolveEvent(context);

        verify(player, times(1)).giveResources(p1);
        verify(opponent, times(1)).giveResources(p2);
    }

    @Test
    void testPlayerHasMore() {
        board.place(new GoldShipCard(), board.getPositionFromGrid(1,1));
        ResourceBundle p1 = createBundle(LumberResource.class);
        when(player.requestResource(any(), eq(1), any())).thenReturn(p1);

        card.resolveEvent(context);

        verify(player, times(1)).giveResources(p1);
        verify(opponent, times(0)).giveResources(any());
    }

}
