package samuel.card.ship;

import samuel.card.CardID;
import samuel.card.ExpansionCard;
import samuel.card.region.RegionCard;
import samuel.event.player.PlayerTradeEvent;
import samuel.eventmanager.Subscribe;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;
import samuel.resource.resources.GoldResource;

import java.util.List;
import java.util.UUID;

public class LargeTradeShipCard implements ExpansionCard {

    private static final int tradeRatio = 2;

    private static final CardID id = new CardID("ship", "large_trade_ship");
    private final UUID uuid = UUID.randomUUID();


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
        return context.getPhase().equals(Phase.ACTION);
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

    private List<RegionCard> adjacentRegions() {
        return List.of();
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
}
