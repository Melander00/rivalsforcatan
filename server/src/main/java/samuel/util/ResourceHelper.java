package samuel.util;

import samuel.board.BoardPosition;
import samuel.card.region.RegionCard;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.resource.Resource;

public class ResourceHelper {


    /**
     * Helper function for the player implementation
     * - asks which region to increase the resource of.
     * @param player
     * @param resourceClass
     */
    public static int increaseRegionOfChoice(Player player, Class<? extends Resource> resourceClass, int amount) {
        RegionCard region = letPlayerChooseRegion(player, resourceClass, RequestCause.CHOOSE_REGION_TO_INCREASE_RESOURCE);
        if(region != null) {
            return region.increaseResource(amount);
        }
        return amount;
    }

    public static int decreaseRegionOfChoice(Player player, Class<? extends Resource> resourceClass, int amount) {
        // todo: Can introduce deadlock, if the player doesn't have a region to decrease from
        RegionCard region = letPlayerChooseRegion(player, resourceClass, RequestCause.CHOOSE_REGION_TO_DECREASE_RESOURCE);
        if(region != null) {
            return region.decreaseAmount(amount);
        }
        return amount;
    }

    public static RegionCard letPlayerChooseRegion(Player player, Class<? extends Resource> resourceClass, RequestCause cause) {
        BoardPosition position;
        boolean validPosition = false;
        while(!validPosition) {
            position = player.requestBoardPosition(player.getPrincipality().getBoardPositions(), cause);
            if(position.isEmpty()) continue;

            if(position.getCard() instanceof RegionCard region) {
                if(region.getType().equals(resourceClass)) {
                    return region;
                }
                player.directMessage("Invalid region");
                continue;
            }

            player.directMessage("Invalid position, not a region");
        }
        return null;
    }

}
