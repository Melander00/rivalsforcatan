package samuel.event.player.exchange;

import samuel.event.EventID;
import samuel.event.PlayerEvent;
import samuel.player.Player;

public class PlayerExchangeEvent implements PlayerEvent {

    private final static EventID id = new EventID("player", "exchange");

    private final Player player;

    public PlayerExchangeEvent(Player player) {
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
