package samuel.card.building;

import samuel.board.BoardPosition;
import samuel.card.CardID;
import samuel.card.PlaceableCard;
import samuel.card.PointHolder;
import samuel.card.SingletonCard;
import samuel.card.region.RegionCard;
import samuel.event.die.ProductionDieEvent;
import samuel.eventmanager.Subscribe;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.point.PointBundle;
import samuel.point.points.CommercePoint;
import samuel.point.points.ProgressPoint;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.WoolResource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarketplaceBuildingCard implements BuildingCard, SingletonCard, PointHolder {

    private static final CardID id = new CardID("building", "marketplace");

    private final UUID uuid = UUID.randomUUID();

    private Player owner;
    private BoardPosition position;

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
    public PointBundle getPoints() {
        PointBundle bundle = new PointBundle();
        bundle.addPoint(CommercePoint.class, 1);
        return bundle;
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(WoolResource.class, 1);
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
    public void onProductionDieEvent(ProductionDieEvent.Post event) {
        GameContext context = event.getContext();

        List<RegionCard> ownerRegions = regionsWithRoll(owner, event.getRollResults());

        for(Player player : context.getPlayers()) {
            if(player.equals(owner)) continue;

            List<RegionCard> regionsWithRoll = regionsWithRoll(player, event.getRollResults());
            if(regionsWithRoll.size() <= ownerRegions.size()) continue;

            ResourceBundle available = new ResourceBundle();
            for(RegionCard region : regionsWithRoll) {
                available.addResource(region.getType(), 1);
            }

            ResourceBundle toGet = owner.requestResource(available, 1, new RequestCause(RequestCauseEnum.FREE_RESOURCES));
            owner.giveResources(toGet);
        }
    }

    private static List<RegionCard> regionsWithRoll(Player player, int roll) {
        List<RegionCard> regions = new ArrayList<>();
        for(BoardPosition pos : player.getPrincipality()) {
            if(pos.isEmpty()) continue;
            if(pos.getCard() instanceof RegionCard region) {
                if(region.getDiceRoll() == roll) {
                    regions.add(region);
                }
            }
        }
        return regions;
    }

}
