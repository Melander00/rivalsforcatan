package samuel.test.common.card.action;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.board.GridBoard;
import samuel.card.Card;
import samuel.card.ExpansionCard;
import samuel.card.PlaceableCard;
import samuel.card.PlayableCard;
import samuel.card.action.ActionCard;
import samuel.card.action.RelocationActionCard;
import samuel.card.building.BuildingCard;
import samuel.card.building.TollBridgeBuildingCard;
import samuel.card.building.booster.IronFoundryBuildingCard;
import samuel.card.center.SettlementCard;
import samuel.card.hero.AustinHeroCard;
import samuel.card.region.*;
import samuel.card.stack.CardStack;
import samuel.effect.Effect;
import samuel.event.Event;
import samuel.event.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.game.GameContext;
import samuel.introductory.IntrodoctoryPrincipality;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.PlayerHand;
import samuel.player.action.PlayerAction;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.point.Point;
import samuel.point.PointBundle;
import samuel.resource.Resource;
import samuel.resource.ResourceBundle;
import samuel.util.Pair;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RelocationTest {

    @Mock
    private Player player;

    @Mock
    private GameContext context;

    private EventBus eventBus;
    private ActionCard card;
    private Board board;

    @BeforeEach
    void setup() {
        eventBus = new GenericEventBus();
        card = new RelocationActionCard();
        board = GridBoard.createGridBoard(5, 7);
    }

    @Test
    void testExpansions() {
        when(context.getEventBus()).thenReturn(eventBus);
        when(player.requestBoolean(any())).thenReturn(true);
        when(player.getPrincipality()).thenReturn(board);

        board.place(new SettlementCard(), board.getPositionFromGrid(2,1));
        board.place(new SettlementCard(), board.getPositionFromGrid(2,3));
        ExpansionCard c1 = new TollBridgeBuildingCard();
        ExpansionCard c2 = new AustinHeroCard();
        BoardPosition pos1 = board.getPositionFromGrid(1,1);
        BoardPosition pos2 = board.getPositionFromGrid(1,3);
        board.place(c1, pos1);
        board.place(c2, pos2);

        when(player.requestBoardPosition(any(), any()))
                .thenReturn(pos1)
                .thenReturn(pos2);

        doAnswer(args -> {
            PlaceableCard card = args.getArgument(0);
            BoardPosition pos = args.getArgument(1);
            GameContext ctx = args.getArgument(2);

            board.place(card, pos);
            card.onPlace(player, ctx, pos);

            return null;
        }).when(player).placeCard(any(), any(), any());

        doAnswer(args -> {
            PlaceableCard card = args.getArgument(0);
            BoardPosition pos = args.getArgument(1);
            GameContext ctx = args.getArgument(2);

            pos.setCard(null);
            card.onRemove(context);

            return null;
        }).when(player).removeCard(any(), any(), any());

        card.onPlay(player, context);

        assertEquals(c1, pos2.getCard());
        assertEquals(c2, pos1.getCard());
    }

    @Test
    void testRegions() {

        when(context.getEventBus()).thenReturn(eventBus);
        when(player.requestBoolean(any())).thenReturn(false);
        when(player.getPrincipality()).thenReturn(board);

        board.place(new SettlementCard(), board.getPositionFromGrid(2,2));
        RegionCard c1 = new GoldFieldRegionCard(1);
        RegionCard c2 = new ForestRegionCard(1);
        BoardPosition pos1 = board.getPositionFromGrid(1,1);
        BoardPosition pos2 = board.getPositionFromGrid(1,3);
        board.place(c1, pos1);
        board.place(c2, pos2);

        when(player.requestBoardPosition(any(), any()))
                .thenReturn(pos1)
                .thenReturn(pos2);

        doAnswer(args -> {
            PlaceableCard card = args.getArgument(0);
            BoardPosition pos = args.getArgument(1);
            GameContext ctx = args.getArgument(2);

            board.place(card, pos);
            card.onPlace(player, ctx, pos);

            return null;
        }).when(player).placeCard(any(), any(), any());

        doAnswer(args -> {
            PlaceableCard card = args.getArgument(0);
            BoardPosition pos = args.getArgument(1);
            GameContext ctx = args.getArgument(2);

            pos.setCard(null);
            card.onRemove(ctx);

            return null;
        }).when(player).removeCard(any(), any(), any());

        card.onPlay(player, context);

        assertEquals(c1, pos2.getCard());
        assertEquals(c2, pos1.getCard());
    }

    @Test
    void testCanPlay() {
        when(context.getPhase()).thenReturn(Phase.ACTION);

        assertTrue(card.canPlay(player, context));
    }

    @Test
    void testCantPlay() {
        when(context.getPhase()).thenReturn(Phase.DICE_ROLL);

        assertFalse(card.canPlay(player, context));
    }

}
