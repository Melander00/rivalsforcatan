package samuel.card.region;

import samuel.card.PlaceableCard;
import samuel.resource.ResourceAmount;

public interface RegionCard extends PlaceableCard {


    ResourceAmount getAmount();

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

    /**
     * Returns the dice roll associated with the region. E.g. the number that the production die need to roll to yield resources.
     * @return
     */
    int getDiceRoll();
}
