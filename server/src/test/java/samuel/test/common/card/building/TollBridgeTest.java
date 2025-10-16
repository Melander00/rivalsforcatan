package samuel.test.common.card.building;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.action.ActionCard;
import samuel.card.action.BrigittaTheWiseWomanActionCard;
import samuel.card.building.BuildingCard;
import samuel.card.building.TollBridgeBuildingCard;
import samuel.event.GenericEventBus;
import samuel.event.die.PlentifulHarvestEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.resources.GoldResource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TollBridgeTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    @Mock
    private BoardPosition position;

    private EventBus eventBus;
    private BuildingCard card;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        card = new TollBridgeBuildingCard();
    }

    @Test
    void testFunctionality() {
        // Arrange
        when(context.getEventBus()).thenReturn(eventBus);
        PlentifulHarvestEvent event = new PlentifulHarvestEvent();

        // Act
        card.onPlace(player, context, position);
        context.getEventBus().fireEvent(event);

        // Assert
        verify(player).giveResources(argThat(bundle -> bundle.getAmount(GoldResource.class) == 2));
    }

    @Test
    void testNoTollBridge() {
        // Arrange
        when(context.getEventBus()).thenReturn(eventBus);
        PlentifulHarvestEvent event = new PlentifulHarvestEvent();

        // Act
        context.getEventBus().fireEvent(event);

        // Assert
        verify(player, times(0)).giveResources(argThat(bundle -> bundle.getAmount(GoldResource.class) == 2));
    }

}
