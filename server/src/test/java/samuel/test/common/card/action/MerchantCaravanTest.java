package samuel.test.common.card.action;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.card.action.ActionCard;
import samuel.card.action.BrigittaTheWiseWomanActionCard;
import samuel.card.action.MerchantCaravanActionCard;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.resource.Resource;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.LumberResource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MerchantCaravanTest {



    /*

        Merchant Caravan Discard exactly 2 of your resources and
        take any 2 resources of your choice in return.

        You may discard 2 resources
        of the same type or 2 different
        resources. The resources
        may come from the same
        or different regions. You
        may also take 2 of the same resource if it seems
        reasonable to you. However, you must have at
        least 2 resources to play the Merchant Caravan

         */

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    private EventBus eventBus;
    private ActionCard card;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        card = new MerchantCaravanActionCard();
    }

    @Test
    void testFunctionality() {
        // Arrange
//        when(context.getPhase()).thenReturn(Phase.ACTION);

        ResourceBundle bundle = createBundle(LumberResource.class, LumberResource.class, LumberResource.class);
        when(player.getResources()).thenReturn(bundle);

        ResourceBundle toRemove = createBundle(LumberResource.class, LumberResource.class);
        when(player.requestResource(eq(bundle), eq(2), any())).thenReturn(toRemove);

        ResourceBundle toGet = createBundle(GoldResource.class, GrainResource.class);
        when(player.requestResource(eq(ResourceBundle.oneOfAll(2)), eq(2), any()))
                .thenReturn(toGet);


        // Act
        card.onPlay(player, context);


        // Assert
        verify(player, times(1)).removeResources(toRemove);
        verify(player, times(1)).giveResources(toGet);
    }

    @Test
    void testNotEnoughResources() {
        // Arrange
        when(context.getPhase()).thenReturn(Phase.ACTION);
        ResourceBundle bundle = createBundle(LumberResource.class);
        when(player.getResources()).thenReturn(bundle);

        // Act

        // Assert
        assertFalse(card.canPlay(player, context));
    }

    @Test
    void testWrongPhase() {
        // Arrange
        when(context.getPhase()).thenReturn(Phase.DICE_ROLL);
//        ResourceBundle bundle = createBundle(LumberResource.class, LumberResource.class, LumberResource.class);
//        when(player.getResources()).thenReturn(bundle);

        // Act

        // Assert
        assertFalse(card.canPlay(player, context));
    }

    @Test
    void testCorrectPhase() {
        // Arrange
        when(context.getPhase()).thenReturn(Phase.ACTION);
        ResourceBundle bundle = createBundle(LumberResource.class, LumberResource.class, LumberResource.class);
        when(player.getResources()).thenReturn(bundle);

        // Act

        // Assert
        assertTrue(card.canPlay(player, context));
    }





    @SafeVarargs
    private static ResourceBundle createBundle(Class<? extends Resource>... resources) {
        ResourceBundle bundle = new ResourceBundle();

        Arrays.stream(resources).forEach(res -> bundle.addResource(res, 1));

        return bundle;
    }
}
