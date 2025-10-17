package samuel.event.player;

import samuel.event.CancelableEvent;
import samuel.event.EventID;
import samuel.event.PlayerEvent;
import samuel.player.Player;

public class PlayerTakeRegionCardsEvent implements PlayerEvent, CancelableEvent {

    // todo

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void cancel() {

    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public EventID getId() {
        return null;
    }
}
