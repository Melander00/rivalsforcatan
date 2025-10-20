package samuel.event.player.replenish;

import samuel.event.EventID;
import samuel.event.PlayerEvent;
import samuel.player.Player;

public class PlayerReplenishEvent implements PlayerEvent {

    private final static EventID id = new EventID("player", "replenish");

    private final Player player;

    public PlayerReplenishEvent(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public EventID getId() {
        return id;
    }
}
