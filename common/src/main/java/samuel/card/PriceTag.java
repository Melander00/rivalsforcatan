package samuel.card;

import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.ResourceBundle;

/**
 * For anything that has a cost attached to it.
 */
public interface PriceTag {

    /**
     * Returns the resources this costs.
     * @return
     */
    ResourceBundle getCost();

}
