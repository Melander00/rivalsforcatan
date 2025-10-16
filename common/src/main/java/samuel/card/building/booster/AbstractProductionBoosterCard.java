package samuel.card.building.booster;

import samuel.board.BoardPosition;
import samuel.card.region.RegionCard;
import samuel.event.die.ProductionDieEvent;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.resource.Resource;

import java.util.List;

public abstract class AbstractProductionBoosterCard implements ProductionBoosterCard {

    private final Class<? extends Resource> resourceType;
    private Player owner = null;

    public AbstractProductionBoosterCard(Class<? extends Resource> resourceType) {
        this.resourceType = resourceType;
    }


    @Override
    public boolean canPlay(Player player, GameContext context) {
        return context.getPhase().equals(Phase.ACTION);
    }

    @Override
    public void onPlace(Player owner, GameContext context, BoardPosition position) {
        this.owner = owner;
    }

    @Override
    public void onRemove(GameContext context) {
        this.owner = null;
    }

    private List<RegionCard> adjacentRegions() {
        return List.of();
    }

    public void onProductionEvent(ProductionDieEvent.Post event) {
        for(RegionCard region : adjacentRegions()) {
            if(region.getType().equals(resourceType) && region.getDiceRoll() == event.getRollResults()) {
                region.increaseResource(1);
            }
        }
    }
}
