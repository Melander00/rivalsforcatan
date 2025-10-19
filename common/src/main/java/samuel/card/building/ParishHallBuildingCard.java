package samuel.card.building;

import samuel.board.BoardPosition;
import samuel.card.CardID;
import samuel.card.SingletonCard;
import samuel.event.player.exchange.PlayerExchangeSearchEvent;
import samuel.eventmanager.Subscribe;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.GrainResource;

import java.util.UUID;

public class ParishHallBuildingCard implements BuildingCard, SingletonCard {

    private static final CardID id = new CardID("building", "parish_hall");

    private final UUID uuid = UUID.randomUUID();

    private Player owner;
    private BoardPosition position;

    private static final int resourcesToPay = 1;

    @Override
    public boolean canPlay(Player player, GameContext context) {
        boolean phase = context.getPhase().equals(Phase.ACTION);
        boolean canPay = player.hasResources(getCost());
        boolean unique = !player.getPrincipality().existsById(id);

        return phase && canPay && unique;
    }

    @Override
    public CardID getCardID() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(BrickResource.class, 1);
        bundle.addResource(GrainResource.class, 1);
        return bundle;
    }

    @Override
    public void onPlace(Player owner, GameContext context, BoardPosition position) {
        this.owner = owner;
        this.position = position;
        context.getEventBus().register(this);
    }

    @Override
    public void onRemove(GameContext context) {
        this.owner = null;
        this.position = null;
        context.getEventBus().unregister(this);
    }


    @Subscribe
    public void onExchangeSearchEvent(PlayerExchangeSearchEvent event) {
        if(event.getPlayer().equals(owner)) {
            if(event.getResourcesToPay() > resourcesToPay) {
                event.setResourcesToPay(resourcesToPay);
            }
        }
    }
}
