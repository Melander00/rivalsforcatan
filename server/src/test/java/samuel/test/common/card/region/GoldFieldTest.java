package samuel.test.common.card.region;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.BoardPosition;
import samuel.card.region.ForestRegionCard;
import samuel.card.region.GoldFieldRegionCard;
import samuel.card.region.RegionCard;
import samuel.event.GenericEventBus;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.LumberResource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GoldFieldTest {

    @Mock private Player player;

    @Mock private GameContext context;

    private RegionCard card;
    private EventBus eventBus;

    @BeforeEach
    void setup() {
        card = null;
        eventBus = new GenericEventBus();
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6})
    void testProduction(int roll) {
        when(context.getEventBus()).thenReturn(eventBus);
        card = new GoldFieldRegionCard(roll);

        card.onPlace(player, context, mock(BoardPosition.class));
        eventBus.fireEvent(new ProductionDieEvent.Post(roll, context));

        assertEquals(1, card.getResources().getAmount(GoldResource.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {2,3,4,5,6})
    void testWrongRoll(int roll) {
        when(context.getEventBus()).thenReturn(eventBus);
        card = new GoldFieldRegionCard(1);

        card.onPlace(player, context, mock(BoardPosition.class));
        eventBus.fireEvent(new ProductionDieEvent.Post(roll, context));

        assertEquals(0, card.getResources().getAmount(GoldResource.class));
    }

}
