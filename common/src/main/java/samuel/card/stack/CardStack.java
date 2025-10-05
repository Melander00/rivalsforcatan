package samuel.card.stack;

import samuel.card.Card;

import java.util.List;

public interface CardStack<CardType extends Card> extends Iterable<CardType> {

    void addCardToBottom(CardType card);
    CardType takeTopCard();
    void shuffleCards();
    List<CardType> getAllCards();
}
