package samuel.card.event;

import samuel.eventmanager.EventBus;
import samuel.event.card.InventionEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.util.CardID;

import java.util.List;

public class InventionEventCard implements EventCard {

    private static final CardID id = new CardID("event", "invention");

    @Override
    public CardID getCardID() {
        return id;
    }

    private final EventBus eventBus;

    public InventionEventCard(GameContext context) {
        this.eventBus = context.getEventBus();
    }

    @Override
    public void resolveEvent(GameContext context) {
        InventionEvent event = new InventionEvent();
        eventBus.fireEvent(event);
        if(event.isCanceled()) return;

        List<Player> players = context.getPlayers(); // todo

        eventBus.fireEvent(new InventionEvent.Post());
    }
}
