package samuel.test.common.card.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.event.EventCard;
import samuel.card.event.InventionEventCard;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.point.points.ProgressPoint;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.LumberResource;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class InventionTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    private Board board;
    private EventCard card;
    private EventBus eventBus;

    @BeforeEach
    void setup() {
        board = GridBoard.createGridBoard(5,7);
        card = new InventionEventCard();
        eventBus = new GenericEventBus();

        when(context.getPlayers()).thenReturn(List.of(player));
        when(context.getEventBus()).thenReturn(eventBus);
    }

    @Test
    void testNoProgressPoints() {
        when(player.getPoints(ProgressPoint.class)).thenReturn(0);

        card.resolveEvent(context);

        verify(player, times(0)).giveResources(any());
    }

    @Test
    void testOneProgressPoint() {
        when(player.getPoints(ProgressPoint.class)).thenReturn(1);
        ResourceBundle bundle = createBundle(LumberResource.class, LumberResource.class);
        when(player.requestResource(any(), eq(1), any())).thenReturn(bundle);

        card.resolveEvent(context);

        verify(player, times(1)).requestResource(any(), eq(1), any());
        verify(player, times(1)).giveResources(bundle);
    }

    @Test
    void testTwoProgressPoints() {
        when(player.getPoints(ProgressPoint.class)).thenReturn(2);
        ResourceBundle bundle = createBundle(LumberResource.class, LumberResource.class);
        when(player.requestResource(any(), eq(2), any())).thenReturn(bundle);

        card.resolveEvent(context);

        verify(player, times(1)).requestResource(any(), eq(2), any());
        verify(player, times(1)).giveResources(bundle);
    }


    @Test
    void testThreeProgressPoints() {
        when(player.getPoints(ProgressPoint.class)).thenReturn(3);
        ResourceBundle bundle = createBundle(LumberResource.class, LumberResource.class);
        when(player.requestResource(any(), eq(2), any())).thenReturn(bundle);

        card.resolveEvent(context);

        verify(player, times(1)).requestResource(any(), eq(2), any());
        verify(player, times(1)).giveResources(bundle);
    }

}
