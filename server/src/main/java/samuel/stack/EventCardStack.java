package samuel.stack;

import samuel.card.event.EventCard;
import samuel.card.event.YuleEventCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventCardStack extends GenericCardStack<EventCard> {

    @Override
    public void shuffleCards() {
        List<EventCard> cards = new ArrayList<>(getCards());

        EventCard yule = null;
        for(EventCard card : cards) {
            if(card instanceof YuleEventCard) {
                yule = card;
                break;
            }
        }
        if(yule == null) {
            super.shuffleCards();
        }

        cards.remove(yule);
        Collections.shuffle(cards);

        List<EventCard> newList = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            if(i > cards.size() - 1) break;

            newList.add(cards.getFirst());
            cards.removeFirst();
        }

        newList.add(yule);

        newList.addAll(cards);

        Collections.reverse(newList);

        setCards(newList);
    }
}
