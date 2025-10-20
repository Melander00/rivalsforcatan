package samuel.event;

import samuel.player.Player;

/**
 * An event that is only applicable to a specific player.
 */
public interface PlayerEvent extends Event {

    /**
     * The player connected with the event.
     * @return
     */
    Player getPlayer();

}
