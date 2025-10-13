package samuel.die.face;

import samuel.die.EventDieFace;
import samuel.game.GameContext;

public class PlentifulHarvestFace implements EventDieFace {
    @Override
    public void resolve(GameContext context) {
        System.out.println("Plentiful harvest!");
    }

    @Override
    public boolean hasPriorityOverProduction() {
        return false;
    }
}
