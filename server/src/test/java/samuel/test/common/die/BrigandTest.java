package samuel.test.common.die;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.region.ForestRegionCard;
import samuel.card.region.GoldFieldRegionCard;
import samuel.card.region.PastureRegionCard;
import samuel.card.region.RegionCard;
import samuel.die.face.BrigandAttackFace;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.LumberResource;
import samuel.resource.resources.WoolResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BrigandTest {

    @Mock
    private GameContext context;

    @Mock
    private Player player;

    private EventBus eventBus;
    private Board board;
    private RegionCard forest;
    private RegionCard goldField;
    private RegionCard pasture;

    @BeforeEach
    void setup() {
        forest = new ForestRegionCard(1);
        goldField = new GoldFieldRegionCard(2);
        pasture = new PastureRegionCard(3);

        board = GridBoard.createGridBoard(5,7);

        board.place(forest, board.getPositionFromGrid(1, 1));
        board.place(goldField, board.getPositionFromGrid(1, 2));
        board.place(pasture, board.getPositionFromGrid(1, 3));

        eventBus = new GenericEventBus();

        when(context.getEventBus()).thenReturn(eventBus);
        when(context.getPlayers()).thenReturn(List.of(player));
        when(player.getPrincipality()).thenReturn(board);
    }

    @ParameterizedTest
    @CsvSource({"3,3,3", "2,3,3", "3,3,2", "3,2,3"})
    void testResourcesMoreThan7(int r1, int r2, int r3) {
        forest.increaseResource(r1);
        goldField.increaseResource(r2);
        pasture.increaseResource(r3);

        (new BrigandAttackFace()).resolve(context);

        assertEquals(r1, forest.getResources().getAmount(LumberResource.class));
        assertEquals(0, goldField.getResources().getAmount(GoldResource.class));
        assertEquals(0, pasture.getResources().getAmount(WoolResource.class));
    }

    @ParameterizedTest
    @CsvSource({"1,3,3", "2,2,3", "2,3,2", "3,1,2", "3,2,1"})
    void testResourcesEqual7(int r1, int r2, int r3) {
        forest.increaseResource(r1);
        goldField.increaseResource(r2);
        pasture.increaseResource(r3);

        (new BrigandAttackFace()).resolve(context);

        assertEquals(r1, forest.getResources().getAmount(LumberResource.class));
        assertEquals(r2, goldField.getResources().getAmount(GoldResource.class));
        assertEquals(r3, pasture.getResources().getAmount(WoolResource.class));
    }


    @ParameterizedTest
    @CsvSource({"0,3,3", "2,0,3", "2,3,0", "0,0,2", "3,3,0"})
    void testResourcesLessThan7(int r1, int r2, int r3) {
        forest.increaseResource(r1);
        goldField.increaseResource(r2);
        pasture.increaseResource(r3);

        (new BrigandAttackFace()).resolve(context);

        assertEquals(r1, forest.getResources().getAmount(LumberResource.class));
        assertEquals(r2, goldField.getResources().getAmount(GoldResource.class));
        assertEquals(r3, pasture.getResources().getAmount(WoolResource.class));
    }

}
