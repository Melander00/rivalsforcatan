package samuel.die.face;

import samuel.card.CardID;
import samuel.die.EventDieFace;
import samuel.game.GameContext;

public class TradeFace implements EventDieFace {

    private static final CardID id = new CardID("event_face", "trade");

    @Override
    public void resolve(GameContext context) {
        // If you have the trade advantage,
        //you receive 1 resource of your choice
        //from your opponent.
        // todo
    }

    @Override
    public boolean hasPriorityOverProduction() {
        return false;
    }

    @Override
    public CardID getId() {
        return id;
    }
}
