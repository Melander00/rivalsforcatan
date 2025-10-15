package samuel.event.player;

import samuel.event.EventID;
import samuel.event.PlayerEvent;
import samuel.player.Player;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;

public class PlayerTradeEvent implements PlayerEvent {

    private static final EventID id = new EventID("player", "trade");

    private final Player player;

    private ResourceAmount toPay;
    private ResourceAmount toGet;

    public PlayerTradeEvent(Player player, ResourceAmount toPay, ResourceAmount toGet) {
        this.player = player;
        this.toPay = toPay;
        this.toGet = toGet;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public EventID getId() {
        return id;
    }

    public ResourceAmount getResourceToPay() {
        return toPay;
    }

    public void setResourceToPay(ResourceAmount amount) {
        this.toPay = amount;
    }

    public ResourceAmount getResourceToGet() {
        return toGet;
    }

    public void setResourceToGet(ResourceAmount amount) {
        this.toGet = amount;
    }

    public static class Post implements PlayerEvent {
        private static final EventID id = new EventID("player", "trade_post");

        private final Player player;

        private final ResourceAmount toPay;
        private final ResourceAmount toGet;

        public Post(Player player, ResourceAmount toPay, ResourceAmount toGet) {
            this.player = player;
            this.toPay = toPay;
            this.toGet = toGet;
        }

        @Override
        public Player getPlayer() {
            return player;
        }

        @Override
        public EventID getId() {
            return id;
        }

        public ResourceAmount getResourceToPay() {
            return toPay;
        }


        public ResourceAmount getResourceToGet() {
            return toGet;
        }
    }
}
