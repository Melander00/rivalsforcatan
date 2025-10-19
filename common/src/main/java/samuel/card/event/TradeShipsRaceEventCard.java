package samuel.card.event;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.CardID;
import samuel.card.ship.TradeShipCard;
import samuel.event.card.TradeShipsRaceEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.resource.ResourceBundle;

import java.util.*;

public class TradeShipsRaceEventCard implements EventCard {

    private static final CardID id = new CardID("event", "trade_ships_race");

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
        context.getEventBus().fireEvent(new TradeShipsRaceEvent());

        List<Player> winners = new ArrayList<>();
        int mostShips = 0;

        for(Player player : context.getPlayers()) {
            int ships = 0;
            Board board = player.getPrincipality();
            for(BoardPosition pos : board) {
                if(pos.isEmpty()) continue;
                if(pos.getCard() instanceof TradeShipCard) {
                    ships++;
                }
            }
            if(ships > 0) {

                if(ships == mostShips) {
                    winners.add(player);
                }

                if(ships > mostShips) {
                    winners.clear();
                    winners.add(player);
                    mostShips = ships;
                }
            }
        }

        if(winners.isEmpty()) return; // No player has a trade ship

        for(Player player : winners) {
            ResourceBundle toGet = player.requestResource(ResourceBundle.oneOfAll(1), 1, new RequestCause(RequestCauseEnum.FREE_RESOURCES));
            player.giveResources(toGet);
        }
    }
}