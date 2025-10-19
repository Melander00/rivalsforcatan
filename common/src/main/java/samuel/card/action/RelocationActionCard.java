package samuel.card.action;

import samuel.board.BoardPosition;
import samuel.card.CardID;
import samuel.card.PlaceableCard;
import samuel.card.util.ExpansionCardHelper;
import samuel.card.util.RegionCardHelper;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RelocationActionCard implements ActionCard {

    private static final CardID id = new CardID("action", "relocation");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public boolean canPlay(Player player, GameContext context) {
        return context.getPhase().equals(Phase.ACTION);
    }

    @Override
    public CardID getCardID() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void onPlay(Player player, GameContext context) {
        boolean moveExpansions = player.requestBoolean(new RequestCause(RequestCauseEnum.RELOCATION_SHOULD_MOVE_EXPANSION));
        if(moveExpansions) {
            moveExpansions(player, context);
        } else {
            moveRegions(player, context);
        }
    }

    private void moveRegions(Player player, GameContext context) {
        BoardPosition from = getPosition(player, RequestCauseEnum.RELOCATION_FROM, RegionCardHelper::validatePlacement);
        BoardPosition to = getPosition(player, RequestCauseEnum.RELOCATION_TO, RegionCardHelper::validatePlacement);

        PlaceableCard card1 = from.getCard();
        PlaceableCard card2 = to.getCard();

        player.removeCard(card1, from, context);
        player.removeCard(card2, to, context);

        player.placeCard(card1, to, context);
        player.placeCard(card2, from, context);

    }

    private void moveExpansions(Player player, GameContext context) {
        BoardPosition from = getPosition(player, RequestCauseEnum.RELOCATION_FROM, ExpansionCardHelper::validatePlacement);
        BoardPosition to = getPosition(player, RequestCauseEnum.RELOCATION_TO, ExpansionCardHelper::validatePlacement);

        PlaceableCard card1 = from.getCard();
        PlaceableCard card2 = to.getCard();

        player.removeCard(card1, from, context);
        player.removeCard(card2, to, context);

        player.placeCard(card1, to, context);
        player.placeCard(card2, from, context);
    }

    public BoardPosition getPosition(Player player, RequestCauseEnum cause, Predicate<BoardPosition> positionTester) {
        while(true) {
            BoardPosition pos = player.requestBoardPosition(player.getPrincipality().getBoardPositions(), new RequestCause(cause));
            if(positionTester.test(pos)) {
                return pos;
            }
        }
    }
}
