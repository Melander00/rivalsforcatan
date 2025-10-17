package samuel.test.common.card.hero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.GridBoard;
import samuel.card.hero.AustinHeroCard;
import samuel.card.hero.HeroCard;
import samuel.card.hero.OsmundHeroCard;
import samuel.game.GameContext;
import samuel.network.SocketClient;
import samuel.player.Player;
import samuel.player.PlayerHand;
import samuel.player.ServerPlayer;
import samuel.point.PointBundle;
import samuel.point.points.SkillPoint;
import samuel.point.points.StrengthPoint;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OsmundTest {

    @Mock
    private SocketClient client;

    @Mock
    private PlayerHand hand;

    @Mock
    private GameContext context;

    private Player player;
    private Board board;

    private HeroCard hero;

    private final int strengthPoints = 2;
    private final int skillPoints = 2;

    @BeforeEach
    void setup() {
        board = GridBoard.createGridBoard(5,7);
        player = new ServerPlayer(board, hand, client);
        hero = new OsmundHeroCard();
    }

    @Test
    void testPoints() {

        board.place(hero, board.getPositionFromGrid(1,1));
        hero.onPlace(player, context, board.getPositionFromGrid(1,1));

        PointBundle bundle = player.getPoints();
        assertEquals(strengthPoints, bundle.getAmount(StrengthPoint.class));
        assertEquals(skillPoints, bundle.getAmount(SkillPoint.class));
    }


}
