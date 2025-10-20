package samuel.card.event;

import samuel.board.BoardPosition;
import samuel.card.CardID;
import samuel.card.PlaceableCard;
import samuel.card.building.BuildingCard;
import samuel.event.card.FeudEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;

import java.util.*;

public class FeudEventCard implements EventCard {

    private static final CardID id = new CardID("event", "feud");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public CardID getCardID() {
        return id;
    }


    @Override
    public void resolveEvent(GameContext context) {
        context.getEventBus().fireEvent(new FeudEvent());

        Player advantage = context.getStrengthAdvantage();
        if(advantage == null) return;

        for(Player player : context.getPlayers()) {
            if(player.equals(advantage)) continue;

            List<BoardPosition> selection = new ArrayList<>();
            for(BoardPosition pos : player.getPrincipality()) {
                if(pos.isEmpty()) continue;

                if(pos.getCard() instanceof BuildingCard card) {
                    selection.add(pos);
                }
            }

            if(selection.isEmpty()) continue;

            if(selection.size() == 1) {
                BoardPosition pos = selection.getFirst();
                player.removeCard(pos.getCard(), pos, context);
                continue;
            }

            if(selection.size() > 3) {
                selection.clear();
                int positionsLeft = 3;
                while(positionsLeft > 0) {
                    BoardPosition pos = advantage.requestBoardPosition(player.getPrincipality().getBoardPositions(), new RequestCause(RequestCauseEnum.FEUD_SELECT_BUILDINGS));
                    if(!pos.isEmpty() && pos.getCard() instanceof BuildingCard) {
                        selection.add(pos);
                        positionsLeft--;
                    }
                }
            }

            // ask player which to remove
            Map<PlaceableCard, BoardPosition> map = new HashMap<>();
            for(BoardPosition pos : selection) {
                map.put(pos.getCard(), pos);
            }

            List<PlaceableCard> cards = map.keySet().stream().toList();

            PlaceableCard toRemove = player.requestCard(cards, new RequestCause(RequestCauseEnum.FEUD_REMOVE_CARD));
            BoardPosition position = map.get(toRemove);
            if(position == null) {
                throw new IllegalStateException("We got a position that was invalid somehow");
            }
            player.removeCard(toRemove, position, context);
        }
    }
}
