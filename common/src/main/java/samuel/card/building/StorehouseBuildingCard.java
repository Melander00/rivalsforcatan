package samuel.card.building;

import samuel.board.BoardPosition;
import samuel.card.CardID;
import samuel.card.region.RegionCard;
import samuel.card.util.ExpansionCardHelper;
import samuel.event.die.BrigandAttackEvent;
import samuel.eventmanager.Subscribe;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.LumberResource;
import samuel.resource.resources.WoolResource;

import java.util.List;
import java.util.UUID;

public class StorehouseBuildingCard implements BuildingCard {

    private static final CardID id = new CardID("building", "storehouse");

    private final UUID uuid = UUID.randomUUID();

    private BoardPosition position;
    private Player owner;

    @Override
    public boolean canPlay(Player player, GameContext context) {
        return context.getPhase().equals(Phase.ACTION) && player.hasResources(getCost());
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
        bundle.addResource(LumberResource.class, 1);
        bundle.addResource(WoolResource.class, 1);
        return bundle;
    }

    @Override
    public void onPlace(Player owner, GameContext context, BoardPosition position) {
        this.position = position;
        this.owner = owner;
        context.getEventBus().register(this);
    }

    @Override
    public void onRemove(GameContext context) {
        this.position = null;
        this.owner = null;
        context.getEventBus().unregister(this);
    }

    @Subscribe
    public void onBrigandAttack(BrigandAttackEvent event) {
        if(event.getPlayer().equals(owner)) {
            // get nearby regions
            List<RegionCard> neighbours = ExpansionCardHelper.getNeighbouringRegions(position);
            // remove their resource contributions from event
            for(RegionCard region : neighbours) {
                event.setResourceAmount(region, 0);
            }
        }
    }
}
