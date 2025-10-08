package samuel.effect;

import samuel.eventmanager.EventBus;
import samuel.eventmanager.Subscribe;
import samuel.event.die.ProductionDieEvent;
import samuel.game.GameContext;
import samuel.player.Player;

public class RigProductionDieEffect implements Effect {

    private final Player owner;
    private boolean hasBeenUsed = false;

    public RigProductionDieEffect(Player owner, GameContext context) {
        this.owner = owner;
        context.getEventBus().register(this);
    }

    @Subscribe
    public void onProductionDie(ProductionDieEvent event) {
        if(!owner.equals(event.getRoller())) return;

        int riggedResults = owner.requestInt(1, 6);

        event.setRollResults(riggedResults);
        this.hasBeenUsed = true;
    }

    @Override
    public boolean hasBeenUsed() {
        return hasBeenUsed;
    }
}
