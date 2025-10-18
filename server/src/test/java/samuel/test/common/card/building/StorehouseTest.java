package samuel.test.common.card.building;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.building.BuildingCard;
import samuel.card.building.StorehouseBuildingCard;
import samuel.card.building.booster.GrainMillBuildingCard;
import samuel.card.center.SettlementCard;
import samuel.card.region.*;
import samuel.die.face.BrigandAttackFace;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.network.SocketClient;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.PlayerHand;
import samuel.player.ServerPlayer;
import samuel.resource.resources.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class StorehouseTest {

    @Mock
    private GameContext context;

    @Mock
    private SocketClient client;

    @Mock
    private PlayerHand hand;

    private Player player;

    private EventBus eventBus;
    private Board board;

    private BuildingCard card;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        board = GridBoard.createGridBoard(5,7);
        card = new StorehouseBuildingCard();
        player = new ServerPlayer(board, hand, client);
    }

    @Test
    void testKeepAllResources() {
        ForestRegionCard forest = new ForestRegionCard(1);
        GoldFieldRegionCard goldField = new GoldFieldRegionCard(2);
        PastureRegionCard pasture = new PastureRegionCard(3);

        board.place(forest, board.getPositionFromGrid(1, 1));
        board.place(goldField, board.getPositionFromGrid(1, 3));
        board.place(pasture, board.getPositionFromGrid(1, 5));

        forest.increaseResource(3);
        goldField.increaseResource(3);
        pasture.increaseResource(3);

        when(context.getEventBus()).thenReturn(eventBus);
        board.place(card, board.getPositionFromGrid(1,2));
        card.onPlace(player, context, board.getPositionFromGrid(1,2));

        BrigandAttackFace face = new BrigandAttackFace();
        face.resolve(context);

        assertEquals(3, forest.getResources().getAmount(LumberResource.class));
        assertEquals(3, goldField.getResources().getAmount(GoldResource.class));
        assertEquals(3, pasture.getResources().getAmount(WoolResource.class));
    }

    @Test
    void testLoseResources() {
        ForestRegionCard forest = new ForestRegionCard(1);
        GoldFieldRegionCard goldField = new GoldFieldRegionCard(2);
        PastureRegionCard pasture = new PastureRegionCard(3);
        MountainsRegionCard mountains = new MountainsRegionCard(4);

        board.place(forest, board.getPositionFromGrid(1, 1));
        board.place(goldField, board.getPositionFromGrid(1, 2));
        board.place(mountains, board.getPositionFromGrid(1, 3));
        board.place(pasture, board.getPositionFromGrid(1, 4));

        forest.increaseResource(3);
        goldField.increaseResource(3);
        mountains.increaseResource(3);
        pasture.increaseResource(3);

        when(context.getEventBus()).thenReturn(eventBus);
        when(context.getPlayers()).thenReturn(List.of(player));

        board.place(card, board.getPositionFromGrid(1,5));
        card.onPlace(player, context, board.getPositionFromGrid(1,5));

        BrigandAttackFace face = new BrigandAttackFace();
        face.resolve(context);

        assertEquals(3, forest.getResources().getAmount(LumberResource.class));
        assertEquals(3, mountains.getResources().getAmount(OreResource.class));
        assertEquals(0, goldField.getResources().getAmount(GoldResource.class));
        assertEquals(0, pasture.getResources().getAmount(WoolResource.class));
    }


    @Test
    void testCorrectResources() {
        assertEquals(createBundle(LumberResource.class, WoolResource.class), card.getCost());
    }

    @Test
    void testCanPlay() {
        Player testPlayer = mock(Player.class);
        when(context.getPhase()).thenReturn(Phase.ACTION);
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

}
