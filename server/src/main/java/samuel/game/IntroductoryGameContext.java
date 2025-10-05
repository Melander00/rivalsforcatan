package samuel.game;

import samuel.eventmanager.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.player.Player;

import java.util.List;

public class IntroductoryGameContext implements GameContext {

    private final GenericEventBus eventBus = new GenericEventBus();


    @Override
    public List<Player> getPlayers() {
        return List.of();
    }

    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }
}
