package samuel.card.region;

import samuel.board.BoardPosition;
import samuel.card.PlaceableCard;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractRegionCard<Res extends Resource> implements RegionCard {

    private final Res resourceType;
    private int resourceAmount = 0;
    private int diceRoll = 0;

    public AbstractRegionCard(Res resourceType, int diceRoll) {
        this.resourceType = resourceType;
        this.diceRoll = diceRoll;
    }

    @Override
    public int getDiceRoll() {
        return diceRoll;
    }

    @Override
    public ResourceAmount getAmount() {
        return new ResourceAmount(resourceType, resourceAmount);
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
        return false;
    }


}
