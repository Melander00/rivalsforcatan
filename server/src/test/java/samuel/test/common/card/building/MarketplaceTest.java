package samuel.test.common.card.building;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.building.AbbeyBuildingCard;
import samuel.card.building.BuildingCard;
import samuel.card.building.MarketplaceBuildingCard;
import samuel.card.center.SettlementCard;
import samuel.card.region.ForestRegionCard;
import samuel.card.region.GoldFieldRegionCard;
import samuel.event.GenericEventBus;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.network.SocketClient;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.ServerPlayer;
import samuel.point.points.CommercePoint;
import samuel.point.points.ProgressPoint;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class MarketplaceTest {

    @Mock
    private Player player;

    @Mock
    private Player opponent;

    @Mock
    private GameContext context;

    private EventBus eventBus;

    private Board board;
    private Board opponentBoard;

    private BuildingCard card;

    @BeforeEach
    void setup() {
        board = GridBoard.createGridBoard(5,7);
        opponentBoard = GridBoard.createGridBoard(5,7);
        eventBus = new GenericEventBus();
        card = new MarketplaceBuildingCard();
    }


    @Test
    void testProductionOpponentHasMore() {
        ForestRegionCard op1 = new ForestRegionCard(1);
        GoldFieldRegionCard op2 = new GoldFieldRegionCard(1);
        opponentBoard.place(op1, opponentBoard.getPositionFromGrid(1,1));
        opponentBoard.place(op2, opponentBoard.getPositionFromGrid(1,2));
        ResourceBundle toGet = createBundle(LumberResource.class);
        when(player.requestResource(any(), eq(1), any())).thenReturn(toGet);
        when(player.getPrincipality()).thenReturn(board);
        when(opponent.getPrincipality()).thenReturn(opponentBoard);
        when(context.getEventBus()).thenReturn(eventBus);
        when(context.getPlayers()).thenReturn(List.of(player, opponent));

        board.place(card, board.getPositionFromGrid(1,1));
        card.onPlace(player, context, board.getPositionFromGrid(1,1));
        eventBus.fireEvent(new ProductionDieEvent.Post(1, context));

        verify(player, times(1)).requestResource(eq(createBundle(LumberResource.class, GoldResource.class)), eq(1), any());
        verify(player, times(1)).giveResources(toGet);
    }

    @Test
    void testPoints() {
        Player player = new ServerPlayer(board, null, mock(SocketClient.class));
        board.place(card, board.getPositionFromGrid(1,1));

        assertEquals(1, player.getPoints(CommercePoint.class));
    }

    @Test
    void testCorrectResources() {
        assertEquals(createBundle(GrainResource.class, WoolResource.class), card.getCost());
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
        when(player.getPrincipality()).thenReturn(board);

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
        BuildingCard card2 = new MarketplaceBuildingCard();

        board.place(card2, board.getPositionFromGrid(1,1));

        assertFalse(card.canPlay(testPlayer, context));
    }

}
