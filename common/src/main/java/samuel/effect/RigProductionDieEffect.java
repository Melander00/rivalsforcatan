package samuel.effect;

import samuel.eventmanager.EventBus;
import samuel.eventmanager.Subscribe;
import samuel.event.die.ProductionDieEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.player.request.RequestCause;

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

        int riggedResults = owner.requestInt(1, 6, RequestCause.RIG_PRODUCTION_DIE);

        event.setRollResults(riggedResults);
        this.hasBeenUsed = true;

        owner.removeEffect(this);
    }

    @Override
    public boolean hasBeenUsed() {
        return hasBeenUsed;
    }
}
