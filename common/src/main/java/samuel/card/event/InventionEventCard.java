package samuel.card.event;

import samuel.eventmanager.EventBus;
import samuel.event.card.InventionEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.card.CardID;

import java.util.List;
import java.util.UUID;

public class InventionEventCard implements EventCard {

    private static final CardID id = new CardID("event", "invention");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public CardID getCardID() {
        return id;
    }

    public InventionEventCard() {
    }

    @Override
    public void resolveEvent(GameContext context) {
        InventionEvent event = new InventionEvent();
        context.getEventBus().fireEvent(event);
        if(event.isCanceled()) return;

        List<Player> players = context.getPlayers(); // todo

        context.getEventBus().fireEvent(new InventionEvent.Post());
    }
}
