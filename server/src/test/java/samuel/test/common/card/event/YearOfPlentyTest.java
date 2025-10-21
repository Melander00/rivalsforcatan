package samuel.test.common.card.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.building.AbbeyBuildingCard;
import samuel.card.building.BuildingCard;
import samuel.card.building.StorehouseBuildingCard;
import samuel.card.event.EventCard;
import samuel.card.event.YearOfPlentyEventCard;
import samuel.card.region.ForestRegionCard;
import samuel.card.region.MountainsRegionCard;
import samuel.card.region.RegionCard;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.resources.LumberResource;
import samuel.resource.resources.OreResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class YearOfPlentyTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    private Board board;
    private EventCard card;
    private EventBus eventBus;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        board = GridBoard.createGridBoard(5,7);
        card = new YearOfPlentyEventCard();

        when(context.getPlayers()).thenReturn(List.of(player));
        when(player.getPrincipality()).thenReturn(board);
        when(context.getEventBus()).thenReturn(eventBus);
    }

    @Test
    void testStorehouse() {
        RegionCard region = new ForestRegionCard(1);
        board.place(region, board.getPositionFromGrid(1,1));
        BuildingCard building = new StorehouseBuildingCard();
        board.place(building, board.getPositionFromGrid(1,2));

        card.resolveEvent(context);

        assertEquals(1, region.getResources().getAmount(LumberResource.class));
    }

    @Test
    void testAbbey() {
        RegionCard region = new ForestRegionCard(1);
        board.place(region, board.getPositionFromGrid(1,1));
        BuildingCard building = new AbbeyBuildingCard();
        board.place(building, board.getPositionFromGrid(1,2));

        card.resolveEvent(context);

        assertEquals(1, region.getResources().getAmount(LumberResource.class));
    }

    @Test
    void testAbbeyBelow() {
        RegionCard region = new ForestRegionCard(1);
        board.place(region, board.getPositionFromGrid(3,3));
        RegionCard mountains = new MountainsRegionCard(2);
        board.place(mountains, board.getPositionFromGrid(3,5));

        BuildingCard building = new AbbeyBuildingCard();
        board.place(building, board.getPositionFromGrid(3,4));

        card.resolveEvent(context);

        assertEquals(1, region.getResources().getAmount(LumberResource.class));
        assertEquals(1, mountains.getResources().getAmount(OreResource.class));
    }

    @Test
    void testBothAbbeyAndStorehouse() {
        BuildingCard building = new AbbeyBuildingCard();
        board.place(building, board.getPositionFromGrid(1,1));
        RegionCard region = new ForestRegionCard(1);
        board.place(region, board.getPositionFromGrid(1,2));
        BuildingCard building2 = new StorehouseBuildingCard();
        board.place(building2, board.getPositionFromGrid(1,3));

        card.resolveEvent(context);

        assertEquals(2, region.getResources().getAmount(LumberResource.class));
    }
}
