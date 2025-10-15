package samuel.card.event;

import samuel.eventmanager.EventBus;
import samuel.event.card.InventionEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.card.CardID;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.point.points.ProgressPoint;
import samuel.resource.ResourceBundle;

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

        List<Player> players = context.getPlayers();
        for(Player player : players) {
            int times = player.getPoints(ProgressPoint.class);
            while(times > 0) {
                ResourceBundle bundle = player.requestResource(ResourceBundle.oneOfAll(), 1, new RequestCause(RequestCauseEnum.FREE_RESOURCE));
                player.giveResources(bundle);
                times--;
            }
        }

        context.getEventBus().fireEvent(new InventionEvent.Post());
    }
}
