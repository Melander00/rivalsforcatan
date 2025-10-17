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
import samuel.card.building.booster.LumberCampBuildingCard;
import samuel.card.region.ForestRegionCard;
import samuel.card.region.HillsRegionCard;
import samuel.card.region.MountainsRegionCard;
import samuel.card.region.RegionCard;
import samuel.event.GenericEventBus;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.LumberResource;
import samuel.resource.resources.OreResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LumberCampTest {
    @Mock
    private GameContext context;

    @Mock
    private Player player;

    private EventBus eventBus;
    private Board board;
    private RegionCard forest;
    private RegionCard hills;

    private final int roll = 1;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        forest = new ForestRegionCard(roll);
        hills = new HillsRegionCard(2);
        board = GridBoard.createGridBoard(5,7);

        when(context.getEventBus()).thenReturn(eventBus);

        board.place(forest, board.getPositionFromGrid(1, 1));
        forest.onPlace(player, context, board.getPositionFromGrid(1, 1));
        board.place(hills, board.getPositionFromGrid(1, 3));
        hills.onPlace(player, context, board.getPositionFromGrid(1, 3));
    }


    @Test
    void testProduction() {
        BuildingCard foundry = new LumberCampBuildingCard();

        board.place(foundry, board.getPositionFromGrid(1, 2));
        foundry.onPlace(player, context, board.getPositionFromGrid(1, 2));
        eventBus.fireEvent(new ProductionDieEvent.Post(roll));

        assertEquals(0, hills.getResources().getAmount(BrickResource.class));
        assertEquals(2, forest.getResources().getAmount(LumberResource.class));
    }

}
