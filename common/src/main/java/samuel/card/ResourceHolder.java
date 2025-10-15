package samuel.card;

import samuel.resource.ResourceBundle;

public interface ResourceHolder {
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
}
