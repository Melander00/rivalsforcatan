package samuel.die.face;

import samuel.card.CardID;
import samuel.die.EventDieFace;
import samuel.game.GameContext;

public class CelebrationFace implements EventDieFace {

    private static final CardID id = new CardID("event_face", "celebration");

    @Override
    public void resolve(GameContext context) {
        // If you have the most skill points,
        //you alone receive 1 resource of your choice.
        //Otherwise, each player receives 1 resource of
        //his choice.
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

