package samuel.test.common.card.hero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.center.SettlementCard;
import samuel.card.hero.AustinHeroCard;
import samuel.card.hero.HeroCard;
import samuel.card.hero.IngaHeroCard;
import samuel.game.GameContext;
import samuel.network.SocketClient;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.PlayerHand;
import samuel.player.ServerPlayer;
import samuel.point.PointBundle;
import samuel.point.points.SkillPoint;
import samuel.point.points.StrengthPoint;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.OreResource;
import samuel.resource.resources.WoolResource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static samuel.test.common.ResourceBundleHelper.createBundle;

@ExtendWith(MockitoExtension.class)
public class IngaTest {

    @Mock
    private SocketClient client;

    @Mock
    private PlayerHand hand;

    @Mock
    private GameContext context;

    private Player player;
    private Board board;

    private HeroCard hero;

    private final int strengthPoints = 1;
    private final int skillPoints = 3;

    @BeforeEach
    void setup() {
        board = GridBoard.createGridBoard(5,7);
        player = new ServerPlayer(board, hand, null);
        hero = new IngaHeroCard();
    }

    @Test
    void testPoints() {

        board.place(hero, board.getPositionFromGrid(1,1));
        hero.onPlace(player, context, board.getPositionFromGrid(1,1));

        PointBundle bundle = player.getPoints();
        assertEquals(strengthPoints, bundle.getAmount(StrengthPoint.class));
        assertEquals(skillPoints, bundle.getAmount(SkillPoint.class));
    }

    @Test
    void testCorrectResources() {
        assertEquals(createBundle(GrainResource.class, WoolResource.class, OreResource.class), hero.getCost());
    }

    @Test
    void testCanPlay() {
        Player testPlayer = mock(Player.class);
        when(context.getPhase()).thenReturn(Phase.ACTION);
        when(testPlayer.hasResources(any())).thenReturn(true);

        assertTrue(hero.canPlay(testPlayer, context));
    }

    @Test
    void testCantPlay() {
        when(context.getPhase()).thenReturn(Phase.DICE_ROLL);

        assertFalse(hero.canPlay(player, context));
    }

    @Test
    void testCorrectPlacement() {
        board.place(new SettlementCard(), board.getPositionFromGrid(2,2));

        assertTrue(hero.validatePlacement(board.getPositionFromGrid(1,2)));
    }

    @Test
    void testIncorrectPlacement() {
        assertFalse(hero.validatePlacement(board.getPositionFromGrid(1,2)));
    }
}
