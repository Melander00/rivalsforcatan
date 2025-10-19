package samuel.card.ship;

import samuel.card.ExpansionCard;
import samuel.card.PointHolder;
import samuel.card.PriceTag;
import samuel.event.player.PlayerTradeEvent;
import samuel.resource.Resource;

public interface SingleResourceTradeShipCard extends TradeShipCard {

    Class<? extends Resource> getResourceType();

    void onPlayerTradeEvent(PlayerTradeEvent event);
}
