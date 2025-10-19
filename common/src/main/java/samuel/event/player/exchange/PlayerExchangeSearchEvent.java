package samuel.event.player.exchange;

import samuel.event.EventID;
import samuel.event.PlayerEvent;
import samuel.player.Player;

public class PlayerExchangeSearchEvent implements PlayerEvent {

    private final static EventID id = new EventID("player", "exchange_search");

    private final Player player;
    private int resourcesToPay;

    public PlayerExchangeSearchEvent(Player player, int resourcesToPay) {
        this.player = player;
        this.resourcesToPay = resourcesToPay;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public EventID getId() {
        return id;
    }

    public int getResourcesToPay() {
        return resourcesToPay;
    }

    public void setResourcesToPay(int resourcesToPay) {
        this.resourcesToPay = resourcesToPay;
    }
}
