package samuel.player;

import samuel.card.Card;
import samuel.card.PlayableCard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GenericPlayerHand implements PlayerHand {

    private List<PlayableCard> hand = new ArrayList<>();

    @Override
    public List<PlayableCard> getCards() {
        return hand;
    }

    @Override
    public void removeCard(PlayableCard card) {
        hand.remove(card);
    }

    @Override
    public void addCard(PlayableCard card) {
        hand.add(card);
    }

    @Override
    public int getSize() {
        return hand.size();
    }

    @Override
    public Iterator<PlayableCard> iterator() {
        return hand.iterator();
    }
}
