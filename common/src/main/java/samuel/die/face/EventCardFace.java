package samuel.die.face;

import samuel.card.CardID;
import samuel.die.EventDieFace;
import samuel.event.die.PlentifulHarvestEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.resource.ResourceBundle;

public class EventCardFace implements EventDieFace {

    private static final CardID id = new CardID("event_face", "event_card");

    @Override
    public void resolve(GameContext context) {
        // todo

        // pick a card from the event cards
        // return it to the bottom of the stack
        // resolve the event
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