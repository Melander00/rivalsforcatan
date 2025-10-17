package samuel.test.common.card.action;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import samuel.card.action.ActionCard;
import samuel.card.action.BrigittaTheWiseWomanActionCard;
import samuel.card.action.GoldsmithActionCard;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.resource.Resource;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.LumberResource;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class GoldsmithTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    private EventBus eventBus;
    private ActionCard card;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        card = new GoldsmithActionCard();
    }

    @Test
    void testNotEnoughGold() {
        when(context.getPhase()).thenReturn(Phase.ACTION);
        when(player.getResources(GoldResource.class)).thenReturn(2);

        assertFalse(card.canPlay(player, context));
    }

    @Test
    void testEnoughGold() {
        when(context.getPhase()).thenReturn(Phase.ACTION);
        when(player.getResources(GoldResource.class)).thenReturn(3);

        assertTrue(card.canPlay(player, context));
    }

    @Test
    void testMoreThanEnoughGold() {
        when(context.getPhase()).thenReturn(Phase.ACTION);
        when(player.getResources(GoldResource.class)).thenReturn(4);

        assertTrue(card.canPlay(player, context));
    }

    @Test
    void testFunctionality() {
        ResourceBundle bundle = createBundle(LumberResource.class, LumberResource.class);
        when(player.requestResource(any(), eq(2), any())).thenReturn(bundle);

        card.onPlay(player, context);

        verify(player, times(1)).removeResources(createBundle(GoldResource.class, GoldResource.class, GoldResource.class));
        verify(player, times(1)).requestResource(any(), eq(2), any());
        verify(player, times(1)).giveResources(bundle);
    }

    @Test
    void testCantPlay() {
        when(context.getPhase()).thenReturn(Phase.DICE_ROLL);

        assertFalse(card.canPlay(player, context));
    }

}
