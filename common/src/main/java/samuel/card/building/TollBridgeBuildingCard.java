package samuel.card.building;

import samuel.board.BoardPosition;
import samuel.card.*;
import samuel.card.util.ExpansionCardHelper;
import samuel.event.die.PlentifulHarvestEvent;
import samuel.eventmanager.Subscribe;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.point.PointBundle;
import samuel.point.points.CommercePoint;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.LumberResource;

import java.util.UUID;

public class TollBridgeBuildingCard implements PlaceableCard, PriceTag, PointHolder, ActionPhaseCard {

    private static final CardID cardId = new CardID("building", "toll_bridge");

    private final UUID uuid = UUID.randomUUID();

    private Player owner = null;

    @Override
    public CardID getCardID() {
        return cardId;
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
        bundle.addResource(LumberResource.class, 1);
        bundle.addResource(BrickResource.class, 1);
        return bundle;
    }

    @Override
    public boolean validatePlacement(BoardPosition position) {
        if(!position.isEmpty()) return false;
        return ExpansionCardHelper.validatePlacement(position);
    }

    @Override
    public void onPlace(Player owner, GameContext context, BoardPosition position) {
        this.owner = owner;
        context.getEventBus().register(this);
    }

    @Override
    public void onRemove(GameContext context) {
        this.owner = null;
        context.getEventBus().unregister(this);
    }

    @Subscribe
    public void onPlentifulHarvestEvent(PlentifulHarvestEvent event) {
        if(this.owner != null) {
            ResourceBundle bundle = new ResourceBundle();
            bundle.addResource(GoldResource.class, 2);
            this.owner.giveResources(bundle);
        }
    }

    @Override
    public boolean canPlay(Player player, GameContext context) {
        if(!ActionPhaseCard.super.canPlay(player, context)) return false;

        if(!player.hasResources(getCost())) return false;

        return true;
    }

}
