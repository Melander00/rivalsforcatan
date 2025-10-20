package samuel.card;

import samuel.resource.Resource;
import samuel.resource.ResourceBundle;

/**
 * Card that can hold resources. Example: Regions and Gold Cache (from era of gold)
 */
public interface ResourceHolder extends Card {

    /**
     * Returns the resources currently held by this card.
     * @return
     */
    ResourceBundle getResources();

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
     * Returns the type of resource this card holds.
     * @return
     */
    Class<? extends Resource> getType();
}
