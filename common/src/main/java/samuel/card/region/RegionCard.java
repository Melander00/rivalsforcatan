package samuel.card.region;

import samuel.card.PlaceableCard;
import samuel.card.ResourceHolder;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;

public interface RegionCard extends PlaceableCard, ResourceHolder {



    /**
     * Returns the dice roll associated with the region. E.g. the number that the production die need to roll to yield resources.
     * @return
     */
    int getDiceRoll();

}
