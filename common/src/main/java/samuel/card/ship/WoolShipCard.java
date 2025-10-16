package samuel.card.ship;

import samuel.card.CardID;
import samuel.event.player.PlayerTradeEvent;
import samuel.eventmanager.Subscribe;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.WoolResource;

import java.util.UUID;

public class WoolShipCard extends AbstractTradeShipCard {
    private static final CardID id = new CardID("ship", "wool_ship");
    private final UUID uuid = UUID.randomUUID();

    public WoolShipCard() {
        super(WoolResource.class);
    }

    @Override
    public CardID getCardID() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Subscribe
    @Override
    public void onPlayerTradeEvent(PlayerTradeEvent event) {
        super.onPlayerTradeEvent(event);
    }
}
