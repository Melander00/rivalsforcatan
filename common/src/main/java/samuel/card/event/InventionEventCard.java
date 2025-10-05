package samuel.card.event;

import samuel.card.EventCard;
import samuel.eventmanager.EventBus;
import samuel.event.card.InventionEvent;
import samuel.game.GameContext;
import samuel.player.Player;

import java.util.List;

public class InventionEventCard implements EventCard {

    private EventBus eventManager;

    @Override
    public void resolveEvent(GameContext context) {
        InventionEvent event = new InventionEvent();
        eventManager.fireEvent(event);
        if(event.isCanceled()) return;

        List<Player> players = context.getPlayers();

        eventManager.fireEvent(new InventionEvent.Post());
    }
}
