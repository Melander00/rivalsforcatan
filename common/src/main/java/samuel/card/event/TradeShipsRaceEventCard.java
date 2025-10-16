package samuel.card.event;

import samuel.card.CardID;
import samuel.game.GameContext;

import java.util.UUID;

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
        // todo
    }
}