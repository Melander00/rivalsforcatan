package samuel.util;

import samuel.board.BoardPosition;
import samuel.card.ResourceHolder;
import samuel.card.region.RegionCard;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;

public class ResourceHelper {

    /**
     * Helper function for the player implementation
     * - asks which region to increase the resource of.
     * @param player
     * @param resourceClass
     */
    public static int increaseHolderOfChoice(Player player, Class<? extends Resource> resourceClass, int amount) {
        ResourceHolder region = letPlayerChooseHolder(player, resourceClass, new RequestCause(RequestCauseEnum.CHOOSE_REGION_TO_INCREASE_RESOURCE, new ResourceAmount(resourceClass, 1)));
        if(region != null) {
            return region.increaseResource(amount);
        }
        return amount;
    }

    public static int decreaseHolderOfChoice(Player player, Class<? extends Resource> resourceClass, int amount) {
        ResourceHolder region = letPlayerChooseHolder(player, resourceClass, new RequestCause(RequestCauseEnum.CHOOSE_REGION_TO_DECREASE_RESOURCE, new ResourceAmount(resourceClass, 1)));
        if(region != null) {
            return region.decreaseAmount(amount);
        }
        return amount;
    }

    public static ResourceHolder letPlayerChooseHolder(Player player, Class<? extends Resource> resourceClass, RequestCause cause) {
        // Can introduce deadlock, if the player doesn't have a region to change from or if the player never selects the correct one.
        int tries = 0;
        int maxTries = 10;
        while (tries++ <= maxTries) {
            BoardPosition position = player.requestBoardPosition(player.getPrincipality().getBoardPositions(), cause);
            if (position.isEmpty()) continue;

            if (position.getCard() instanceof ResourceHolder region) {
                if (region.getType().equals(resourceClass)) {
                    return region;
                }
                player.directMessage("Invalid resource holder.");
                continue;
            }

            player.directMessage("Invalid position, not a resource holder.");
        }
        player.directMessage("AUTO_STOP_DEADLOCK: You took too many tries to decide.");
        return null;
    }

}
