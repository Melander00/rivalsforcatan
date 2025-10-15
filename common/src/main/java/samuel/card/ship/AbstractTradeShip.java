package samuel.card.ship;

import samuel.board.BoardPosition;
import samuel.card.util.ExpansionCardHelper;
import samuel.event.player.PlayerTradeEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.TimberResource;
import samuel.resource.resources.WoolResource;

public abstract class AbstractTradeShip implements TradeShipCard {

    private static final int tradeRatio = 2;

    private final Class<? extends Resource> resourceType;
    private Player owner = null;

    public AbstractTradeShip(Class<? extends Resource> resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public boolean canPlay(Player player, GameContext context) {
        return false;
    }

    @Override
    public void onRemove(GameContext context) {
        context.getEventBus().unregister(this);
    }

    @Override
    public void onPlace(Player owner, GameContext context) {
        this.owner = owner;
        context.getEventBus().register(this);
    }

    @Override
    public void onPlayerTradeEvent(PlayerTradeEvent event) {
        ResourceAmount toPay = event.getResourceToPay();
        if(event.getPlayer().equals(owner) && toPay.resourceType().equals(resourceType)) {
            if(toPay.amount() > tradeRatio) {
                event.setResourceToPay(new ResourceAmount(resourceType, tradeRatio));
            }
        }
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(TimberResource.class, 1);
        bundle.addResource(WoolResource.class, 1);
        return bundle;
    }

    @Override
    public Class<? extends Resource> getResourceType() {
        return resourceType;
    }
}
