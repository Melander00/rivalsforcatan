package samuel.die.face;

import samuel.card.CardID;
import samuel.card.event.EventCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.die.EventDieFace;
import samuel.event.die.EventCardEvent;
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
        context.getEventBus().fireEvent(new EventCardEvent(context));

        CardStack<EventCard> stack = context.getStackContainer().getEventStack();

        EventCard card = stack.takeTopCard();
        stack.addCardToBottom(card);
        card.resolveEvent(context);

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