package samuel.effect;

import samuel.eventmanager.Subscribe;
import samuel.event.die.ProductionDieEvent;
import samuel.player.Player;

public class RigProductionDieEffect implements Effect {

    private Player owner;
    private boolean hasBeenUsed = false;

    @Subscribe
    public void onProductionDie(ProductionDieEvent event) {
        if(!owner.equals(event.getRoller())) return;

        // ask the player the production die result
        int riggedResults = 0;

        event.setRollResults(riggedResults);
        this.hasBeenUsed = true;
    }

    @Override
    public boolean hasBeenUsed() {
        return hasBeenUsed;
    }
}
