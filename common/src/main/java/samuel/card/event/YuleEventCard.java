package samuel.card.event;

import samuel.card.CardID;
import samuel.card.stack.CardStack;
import samuel.event.card.YuleEvent;
import samuel.game.GameContext;

import java.util.UUID;

public class YuleEventCard implements EventCard {

    private static final CardID id = new CardID("event", "yule");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public CardID getCardID() {
        return id;
    }


    @Override
    public void resolveEvent(GameContext context) {

        CardStack<EventCard> eventStack = context.getStackContainer().getEventStack();

        context.getEventBus().fireEvent(new YuleEvent());

        eventStack.shuffleCards();

        EventCard card = eventStack.takeTopCard();
        eventStack.addCardToBottom(card);

        card.resolveEvent(context);
    }
}
