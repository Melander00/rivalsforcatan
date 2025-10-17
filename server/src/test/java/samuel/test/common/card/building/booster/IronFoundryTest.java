package samuel.test.common.card.building.booster;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.board.GridBoard;
import samuel.card.ExpansionCard;
import samuel.card.building.booster.IronFoundryBuildingCard;
import samuel.card.region.ForestRegionCard;
import samuel.card.region.RegionCard;
import samuel.event.GenericEventBus;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.player.Player;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Disabled
public class IronFoundryTest {
    @Mock
    private Player player;

    @Mock private GameContext context;

    @Mock private BoardPosition position;

    @Mock private Board board;

    private ExpansionCard card;
    private EventBus eventBus;

    @BeforeEach
    void setup() {
        card = new IronFoundryBuildingCard();
        eventBus = new GenericEventBus();
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6})
    void testProduction(int roll) {
        when(context.getEventBus()).thenReturn(eventBus);
        RegionCard regionCard = new ForestRegionCard(roll);

        card.onPlace(player, context, position);
        eventBus.fireEvent(new ProductionDieEvent.Post(roll));

        // todo
    }

}
