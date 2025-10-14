package samuel.event;

import samuel.player.Player;

public interface PlayerEvent extends Event {

    Player getPlayer();

}
