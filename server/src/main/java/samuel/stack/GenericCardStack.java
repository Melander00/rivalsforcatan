package samuel.stack;

import samuel.card.Card;
import samuel.card.stack.CardStack;

import java.util.*;

public class GenericCardStack<CardType extends Card> implements CardStack<CardType> {

    private final List<CardType> cards = new ArrayList<>();

    private final UUID uuid = UUID.randomUUID();

    @Override
    public void addCardToBottom(CardType card) {
        cards.add(card);
    }

    @Override
    public CardType takeTopCard() {
        return cards.removeFirst();
    }

    @Override
    public CardType takeCardByUuid(UUID uuid) {
        for(CardType card : cards) {
            if(card.getUuid().equals(uuid)) {
                this.cards.remove(card);
                return card;
            }
        }
        return null;
    }

    @Override
    public void shuffleCards() {
        Collections.shuffle(this.cards);
    }

    @Override
    public List<CardType> getCards() {
        return cards;
    }

    @Override
    public int getSize() {
        return cards.size();
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public Iterator<CardType> iterator() {
        return cards.iterator();
    }
}
