package samuel.test.common.card.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.board.GridBoard;
import samuel.card.ExpansionCard;
import samuel.card.building.BuildingCard;
import samuel.card.building.MarketplaceBuildingCard;
import samuel.card.building.ParishHallBuildingCard;
import samuel.card.building.TollBridgeBuildingCard;
import samuel.card.building.booster.IronFoundryBuildingCard;
import samuel.card.event.EventCard;
import samuel.card.event.FeudEventCard;
import samuel.card.hero.AustinHeroCard;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeudTest {

    @Mock
    private Player p1;

    @Mock
    private Player p2;

    private Board b1;
    private Board b2;

    @Mock
    private GameContext context;

    private EventBus eventBus;
    private EventCard card;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        card = new FeudEventCard();
        b1 = GridBoard.createGridBoard(5,7);
        b2 = GridBoard.createGridBoard(5,7);

        when(context.getEventBus()).thenReturn(eventBus);
    }

    @Test
    void testNoPlayerAdvantage() {
        when(context.getStrengthAdvantage()).thenReturn(null);

        card.resolveEvent(context);

        verify(p1, times(0)).removeCard(any(), any(), any());
        verify(p2, times(0)).removeCard(any(), any(), any());
    }

    @Test
    void testNoBuilding() {
        when(context.getStrengthAdvantage()).thenReturn(p1);
        when(context.getPlayers()).thenReturn(List.of(p1, p2));
        when(p2.getPrincipality()).thenReturn(b2);
        BoardPosition pos = b2.getPositionFromGrid(1,1);
        ExpansionCard hero = new AustinHeroCard();
        b2.place(hero, pos);

        card.resolveEvent(context);

        verify(p2, times(0)).removeCard(any(), any(), eq(context));
    }

    @Test
    void testOneBuilding() {
        when(context.getStrengthAdvantage()).thenReturn(p1);
        when(context.getPlayers()).thenReturn(List.of(p1, p2));
        when(p2.getPrincipality()).thenReturn(b2);
        BoardPosition pos = b2.getPositionFromGrid(1,1);
        BuildingCard building = new TollBridgeBuildingCard();
        b2.place(building, pos);

        card.resolveEvent(context);

        verify(p2, times(1)).removeCard(building, pos, context);
    }

    @Test
    void testOneBuildingAndOneHero() {
        when(context.getStrengthAdvantage()).thenReturn(p1);
        when(context.getPlayers()).thenReturn(List.of(p1, p2));
        when(p2.getPrincipality()).thenReturn(b2);

        BoardPosition pos = b2.getPositionFromGrid(1,1);
        BuildingCard building = new TollBridgeBuildingCard();
        b2.place(building, pos);
        b2.place(new AustinHeroCard(), b2.getPositionFromGrid(1,2));

        card.resolveEvent(context);

        verify(p2, times(1)).removeCard(building, pos, context);
    }

    @Test
    void testThreeBuildings() {
        when(context.getStrengthAdvantage()).thenReturn(p1);
        when(context.getPlayers()).thenReturn(List.of(p1, p2));
        when(p2.getPrincipality()).thenReturn(b2);

        BoardPosition pos = b2.getPositionFromGrid(1,1);
        BuildingCard building = new TollBridgeBuildingCard();
        b2.place(building, pos);
        BuildingCard c2 = new MarketplaceBuildingCard();
        BuildingCard c3 = new IronFoundryBuildingCard();
        b2.place(c2, b2.getPositionFromGrid(1,2));
        b2.place(c3, b2.getPositionFromGrid(1,3));

        when(p2.requestCard(anyList(), any())).thenReturn(building);

        card.resolveEvent(context);

        verify(p2, times(1)).removeCard(building, pos, context);
    }

    @Test
    void testMultipleBuildings() {
        when(context.getStrengthAdvantage()).thenReturn(p1);
        when(context.getPlayers()).thenReturn(List.of(p1, p2));
        when(p2.getPrincipality()).thenReturn(b2);

        BoardPosition pos = b2.getPositionFromGrid(1,1);
        BuildingCard building = new TollBridgeBuildingCard();
        b2.place(building, pos);
        BuildingCard c2 = new MarketplaceBuildingCard();
        BuildingCard c3 = new IronFoundryBuildingCard();
        BuildingCard c4 = new ParishHallBuildingCard();
        b2.place(c2, b2.getPositionFromGrid(1,2));
        b2.place(c3, b2.getPositionFromGrid(1,3));
        b2.place(c4, b2.getPositionFromGrid(1,4));
        when(p1.requestBoardPosition(eq(b2.getBoardPositions()), any()))
                .thenReturn(pos)
                .thenReturn(b2.getPositionFromGrid(1,2))
                .thenReturn(b2.getPositionFromGrid(1,4));
        when(p2.requestCard(anyList(), any())).thenReturn(building);

        card.resolveEvent(context);

        verify(p2, times(1)).removeCard(building, pos, context);
    }
}
