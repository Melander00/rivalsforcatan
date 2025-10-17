package samuel.test.common.card.building.booster;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.building.BuildingCard;
import samuel.card.building.booster.GrainMillBuildingCard;
import samuel.card.building.booster.IronFoundryBuildingCard;
import samuel.card.region.FieldsRegionCard;
import samuel.card.region.ForestRegionCard;
import samuel.card.region.MountainsRegionCard;
import samuel.card.region.RegionCard;
import samuel.event.GenericEventBus;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.LumberResource;
import samuel.resource.resources.OreResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrainMillTest {
    @Mock
    private GameContext context;

    @Mock
    private Player player;

    private EventBus eventBus;
    private Board board;
    private RegionCard fields;
    private RegionCard forest;

    private final int roll = 1;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        fields = new FieldsRegionCard(roll);
        forest = new ForestRegionCard(2);
        board = GridBoard.createGridBoard(5,7);

        when(context.getEventBus()).thenReturn(eventBus);

        board.place(fields, board.getPositionFromGrid(1, 1));
        fields.onPlace(player, context, board.getPositionFromGrid(1, 1));
        board.place(forest, board.getPositionFromGrid(1, 3));
        forest.onPlace(player, context, board.getPositionFromGrid(1, 3));
    }


    @Test
    void testProduction() {
        BuildingCard foundry = new GrainMillBuildingCard();

        board.place(foundry, board.getPositionFromGrid(1, 2));
        foundry.onPlace(player, context, board.getPositionFromGrid(1, 2));
        eventBus.fireEvent(new ProductionDieEvent.Post(roll));

        assertEquals(0, forest.getResources().getAmount(LumberResource.class));
        assertEquals(2, fields.getResources().getAmount(GrainResource.class));
    }
}
