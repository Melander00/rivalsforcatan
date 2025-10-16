package samuel.event.die;

import samuel.card.ResourceHolder;
import samuel.event.EventID;
import samuel.event.PlayerEvent;
import samuel.player.Player;
import samuel.resource.ResourceBundle;

import java.util.Map;
import java.util.UUID;

/**
 * Is fired AFTER all resources have been counted but BEFORE the check whether there are more than the threshold.
 */
public class BrigandAttackEvent implements PlayerEvent {
    private final static EventID id = new EventID("dice_event", "brigand_attack");

    private final Player owner;

    private Map<ResourceHolder, Integer> resourceHolders;

    public BrigandAttackEvent(Player player, Map<ResourceHolder, Integer> resourceHolders) {
        this.owner = player;
        this.resourceHolders = resourceHolders;
    }

    @Override
    public Player getPlayer() {
        return owner;
    }

    @Override
    public EventID getId() {
        return id;
    }

    public Map<ResourceHolder, Integer> getResourceHolders() {
        return resourceHolders;
    }

    public void setResourceHolders(Map<ResourceHolder, Integer> resourceHolders) {
        this.resourceHolders = resourceHolders;
    }

    public void removeResources(ResourceHolder card) {
        resourceHolders.remove(card);
    }

    public void setResourceAmount(ResourceHolder card, Integer amount) {
        resourceHolders.put(card, amount);
    }

    /**
     * Is fired after the brigand event has concluded. Includes how many resources were lost.
     */
    public static class Post implements PlayerEvent {
        private final static EventID id = new EventID("dice_event", "brigand_attack_post");

        private final Player owner;

        private final ResourceBundle lost;

        public Post(Player player, ResourceBundle lost) {
            this.owner = player;
            this.lost = lost;
        }

        @Override
        public Player getPlayer() {
            return owner;
        }

        @Override
        public EventID getId() {
            return id;
        }

        public ResourceBundle getLostResources() {
            return lost;
        }
    }
}
