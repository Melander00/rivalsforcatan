package samuel.card.ship;

import samuel.card.CardID;
import samuel.event.player.PlayerTradeEvent;
import samuel.eventmanager.Subscribe;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.OreResource;

import java.util.UUID;

public class OreShipCard extends AbstractTradeShipCard {
    private static final CardID id = new CardID("ship", "ore_ship");
    private final UUID uuid = UUID.randomUUID();

    public OreShipCard() {
        super(OreResource.class);
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