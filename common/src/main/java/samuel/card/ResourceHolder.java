package samuel.card;

import samuel.resource.ResourceAmount;

public interface ResourceHolder {
    ResourceAmount getResourceAmount();
    /**
     * Adds to the resource pile.
     * @param amount
     * @return The amount still left to add after reaching max.
     */
    int increaseResource(int amount);

    /**
     * Removes from the resource pile
     * @param amount
     * @return The amount still left to remove.
     */
    int decreaseAmount(int amount);
}
