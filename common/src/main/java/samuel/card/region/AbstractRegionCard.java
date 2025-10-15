package samuel.card.region;

import samuel.board.BoardPosition;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;

import java.util.UUID;

public abstract class AbstractRegionCard implements RegionCard {

    private final Class<? extends Resource> resourceType;
    private int resourceAmount = 0;
    private int diceRoll = 0;

    private final UUID uuid = UUID.randomUUID();

    public AbstractRegionCard(Class<? extends Resource> resourceType, int diceRoll) {
        this.resourceType = resourceType;
        this.diceRoll = diceRoll;
    }

    @Override
    public Class<? extends Resource> getType() {
        return resourceType;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int getDiceRoll() {
        return diceRoll;
    }

    @Override
    public ResourceBundle getResources() {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(resourceType, resourceAmount);
        return bundle;
    }

    @Override
    public int increaseResource(int amount) {
        int max = this.getMaximumAllowedResources();

        int total = amount + this.resourceAmount;
        if(total > max) {
            this.resourceAmount = max;
            return total - max;
        }

        this.resourceAmount = total;
        return 0;
    }

    private int getMaximumAllowedResources() {
        return 3; // Default, able to be overriden by subclasses.
    }

    @Override
    public int decreaseAmount(int amount) {
        int min = 0;

        int overshoot = amount - this.resourceAmount;
        if(overshoot > min) {
            this.resourceAmount = min;
            return overshoot;
        }

        this.resourceAmount -= amount;
        return 0;
    }

    @Override
    public boolean validatePlacement(BoardPosition position) {
        return true;
    }



    @Override
    public void onPlace(Player owner, GameContext context) {
        context.getEventBus().register(this);
    }

    @Override
    public void onRemove(GameContext context) {
        context.getEventBus().unregister(this);
    }

    @Override
    public boolean canPlay(Player player, GameContext context) {
        return true;
    }

    public ResourceAmount getResourceAmount() {
        return new ResourceAmount(resourceType, resourceAmount);
    }


}
