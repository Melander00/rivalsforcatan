package samuel.card.ship;

import samuel.board.BoardPosition;
import samuel.card.CardID;
import samuel.card.ExpansionCard;
import samuel.card.PointHolder;
import samuel.card.PriceTag;
import samuel.card.region.RegionCard;
import samuel.card.util.ExpansionCardHelper;
import samuel.event.player.PlayerTradeEvent;
import samuel.eventmanager.Subscribe;
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

import java.util.List;
import java.util.UUID;

public class LargeTradeShipCard implements TradeShipCard {

    private static final int tradeRatio = 2;

    private static final CardID id = new CardID("ship", "large_trade_ship");
    private final UUID uuid = UUID.randomUUID();

    private BoardPosition position;

    private Player owner = null;

    @Override
    public CardID getCardID() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean canPlay(Player player, GameContext context) {
        return context.getPhase().equals(Phase.ACTION) && player.hasResources(getCost());
    }

    @Override
    public void onRemove(GameContext context) {
        this.owner = null;
        context.getEventBus().unregister(this);
    }

    @Override
    public void onPlace(Player owner, GameContext context, BoardPosition position) {
        this.owner = owner;
        this.position = position;
        context.getEventBus().register(this);
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(LumberResource.class, 1);
        bundle.addResource(WoolResource.class, 1);
        return bundle;
    }

    private List<RegionCard> adjacentRegions() {
        return ExpansionCardHelper.getNeighbouringRegions(position);
    }

    @Subscribe
    public void onPlayerTradeEvent(PlayerTradeEvent event) {
        ResourceAmount toPay = event.getResourceToPay();
        Class<? extends Resource> resourceType = event.getResourceToPay().resourceType();
        if(event.getPlayer().equals(owner)) {
            for(RegionCard card : adjacentRegions()) {
                if(card.getType().equals(resourceType)) {
                    if(toPay.amount() > tradeRatio) {
                        event.setResourceToPay(new ResourceAmount(resourceType, tradeRatio));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public PointBundle getPoints() {
        PointBundle bundle = new PointBundle();
        bundle.addPoint(CommercePoint.class, 1);
        return bundle;
    }
}
