package samuel.game;

import samuel.eventmanager.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.player.Player;

import java.util.ArrayList;
import java.util.List;

public class IntroductoryGameContext implements GameContext {

    private final GenericEventBus eventBus = new GenericEventBus();

    private final List<Player> players = new ArrayList<>();
    private int activePlayerIndex = 0;

    @Override
    public Player getActivePlayer() {
        if(this.activePlayerIndex > this.players.size() - 1) {
            throw new IllegalStateException("The active player index is bigger than the players size.");
        }
        return this.players.get(this.activePlayerIndex);
    }

    @Override
    public void switchTurn() {
        this.activePlayerIndex = (this.activePlayerIndex + 1) % this.players.size();
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player);
    }

    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }
}
