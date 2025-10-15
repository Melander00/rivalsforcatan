package samuel.card.ship;

import samuel.card.ExpansionCard;
import samuel.card.PointHolder;
import samuel.card.PriceTag;
import samuel.event.player.PlayerTradeEvent;
import samuel.eventmanager.Subscribe;
import samuel.resource.Resource;

public interface TradeShipCard extends ExpansionCard, PriceTag, PointHolder {

    Class<? extends Resource> getResourceType();

    void onPlayerTradeEvent(PlayerTradeEvent event);
}
