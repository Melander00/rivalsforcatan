package samuel.card.ship;

import samuel.card.ExpansionCard;
import samuel.card.PointHolder;
import samuel.card.PriceTag;
import samuel.event.player.PlayerTradeEvent;
import samuel.resource.Resource;

public interface SingleResourceTradeShipCard extends TradeShipCard {

    /**
     * Gets the type of resource that can be traded using this ship.
     * @return
     */
    Class<? extends Resource> getResourceType();

    /**
     * Subscription to the PlayerTradeEvent
     * @param event
     */
    void onPlayerTradeEvent(PlayerTradeEvent event);
}
