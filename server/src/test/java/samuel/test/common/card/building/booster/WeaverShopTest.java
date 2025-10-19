package samuel.test.common.card.building.booster;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.building.BuildingCard;
import samuel.card.building.booster.IronFoundryBuildingCard;
import samuel.card.building.booster.WeaverShopBuildingCard;
import samuel.card.center.SettlementCard;
import samuel.card.region.ForestRegionCard;
import samuel.card.region.MountainsRegionCard;
import samuel.card.region.PastureRegionCard;
import samuel.card.region.RegionCard;
import samuel.event.GenericEventBus;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.LumberResource;
import samuel.resource.resources.OreResource;
import samuel.resource.resources.WoolResource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class WeaverShopTest {
    @Mock
    private GameContext context;

    @Mock
    private Player player;

    private EventBus eventBus;
    private Board board;
    private RegionCard pasture;
    private RegionCard forest;

    private final int roll = 1;

    private BuildingCard booster;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        pasture = new PastureRegionCard(roll);
        forest = new ForestRegionCard(2);
        board = GridBoard.createGridBoard(5,7);
        booster = new WeaverShopBuildingCard();

        when(context.getEventBus()).thenReturn(eventBus);

        board.place(pasture, board.getPositionFromGrid(1, 1));
        pasture.onPlace(player, context, board.getPositionFromGrid(1, 1));
        board.place(forest, board.getPositionFromGrid(1, 3));
        forest.onPlace(player, context, board.getPositionFromGrid(1, 3));
    }


    @Test
    void testProduction() {
        board.place(booster, board.getPositionFromGrid(1, 2));
        booster.onPlace(player, context, board.getPositionFromGrid(1, 2));
        eventBus.fireEvent(new ProductionDieEvent.Post(roll, context));

        assertEquals(0, forest.getResources().getAmount(LumberResource.class));
        assertEquals(2, pasture.getResources().getAmount(WoolResource.class));
    }

    @Test
    void testCorrectResources() {
        assertEquals(createBundle(LumberResource.class, WoolResource.class), booster.getCost());
    }

    @Test
    void testCanPlay() {
        when(context.getPhase()).thenReturn(Phase.ACTION);
        when(player.hasResources(any())).thenReturn(true);

        assertTrue(booster.canPlay(player, context));
    }

    @Test
    void testCantPlay() {
        when(context.getPhase()).thenReturn(Phase.DICE_ROLL);

        assertFalse(booster.canPlay(player, context));
    }

    @Test
    void testCorrectPlacement() {
        board.place(new SettlementCard(), board.getPositionFromGrid(2,2));

        assertTrue(booster.validatePlacement(board.getPositionFromGrid(1,2)));
    }

    @Test
    void testIncorrectPlacement() {
        assertFalse(booster.validatePlacement(board.getPositionFromGrid(1,2)));
    }
}
