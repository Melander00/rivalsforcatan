package samuel.card.ship;

import samuel.event.player.PlayerTradeEvent;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.point.PointBundle;
import samuel.point.points.CommercePoint;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.LumberResource;
import samuel.resource.resources.WoolResource;

public abstract class AbstractTradeShipCard implements TradeShipCard {

    private static final int tradeRatio = 2;

    private final Class<? extends Resource> resourceType;
    private Player owner = null;

    public AbstractTradeShipCard(Class<? extends Resource> resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public boolean canPlay(Player player, GameContext context) {
        return context.getPhase().equals(Phase.ACTION)
    }

    @Override
    public void onRemove(GameContext context) {
        this.owner = null;
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
        bundle.addResource(LumberResource.class, 1);
        bundle.addResource(WoolResource.class, 1);
        return bundle;
    }

    @Override
    public Class<? extends Resource> getResourceType() {
        return resourceType;
    }

    @Override
    public PointBundle getPoints() {
        PointBundle bundle = new PointBundle();
        bundle.addPoint(CommercePoint.class, 1);
        return bundle;
    }
}
