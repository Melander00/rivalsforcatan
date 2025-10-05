package samuel.game;

import samuel.eventmanager.EventBus;
import samuel.player.Player;

import java.util.List;

public interface GameContext {
    List<Player> getPlayers();
    EventBus getEventBus();
}
