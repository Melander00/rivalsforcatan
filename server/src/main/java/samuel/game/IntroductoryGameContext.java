package samuel.game;

import samuel.card.Card;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.condition.IntroductoryVictoryCondition;
import samuel.condition.VictoryCondition;
import samuel.die.Die;
import samuel.die.EventDie;
import samuel.die.EventDieFace;
import samuel.die.ProductionDie;
import samuel.eventmanager.GenericEventBus;
import samuel.eventmanager.EventBus;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.stack.GenericStackContainer;

import java.util.ArrayList;
import java.util.List;

public class IntroductoryGameContext implements GameContext {

    private final GenericEventBus eventBus = new GenericEventBus();

    private final List<Player> players = new ArrayList<>();
    private int activePlayerIndex = 0;

    private final VictoryCondition victoryCondition = new IntroductoryVictoryCondition();

    private final StackContainer stackContainer = new GenericStackContainer();

    private final Die<Integer> productionDie = new ProductionDie();
    private final Die<EventDieFace> eventDie = new EventDie();



    private Phase phase;

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

    @Override
    public boolean hasWon(Player player) {
        return victoryCondition.hasWon(player);
    }

    @Override
    public StackContainer getStackContainer() {
        return stackContainer;
    }

    @Override
    public Phase getPhase() {
        return phase;
    }

    @Override
    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    @Override
    public EventDieFace rollEventDice() {
        return eventDie.rollDie();
    }

    @Override
    public Integer rollProductionDie() {
        return productionDie.rollDie();
    }
}
