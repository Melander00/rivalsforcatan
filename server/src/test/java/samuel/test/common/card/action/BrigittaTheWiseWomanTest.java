package samuel.test.common.card.action;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.card.action.ActionCard;
import samuel.card.action.BrigittaTheWiseWomanActionCard;
import samuel.event.GenericEventBus;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BrigittaTheWiseWomanTest {
    @Mock
    private Player player;

    @Mock
    private GameContext context;

    private EventBus eventBus;
    private ActionCard card;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        card = new BrigittaTheWiseWomanActionCard();
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6})
    void testFunctionality(int roll) {
        // Arrange
        when(player.requestInt(eq(1), eq(6), any())).thenReturn(roll); // Simulate rigging a 5
        when(context.getEventBus()).thenReturn(eventBus);
        ProductionDieEvent event = new ProductionDieEvent(player, 1);

        // Act
        card.onPlay(player, context);
        context.getEventBus().fireEvent(event);

        // Assert
        assertEquals(roll, event.getRollResults());
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5,6})
    void testWrongPlayerRolls(int roll) {
        // Arrange
        Player otherPlayer = mock(Player.class);
        when(context.getEventBus()).thenReturn(eventBus);
        ProductionDieEvent event = new ProductionDieEvent(otherPlayer, roll);

        // Act
        card.onPlay(player, context); // We aren't testing player so using player.play(card, context) is wrong!
        context.getEventBus().fireEvent(event);

        // Assert
        assertEquals(roll, event.getRollResults());
    }

    @Test
    void testCorrectPhase() {
        when(context.getPhase()).thenReturn(Phase.DICE_ROLL);

        assertTrue(card.canPlay(player, context));
    }

    @Test
    void testWrongPhase() {
        when(context.getPhase()).thenReturn(Phase.ACTION);

        assertFalse(card.canPlay(player, context));
    }

}
