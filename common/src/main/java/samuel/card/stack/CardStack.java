package samuel.card.stack;

import samuel.card.Card;

import java.util.List;
import java.util.UUID;

public interface CardStack<CardType extends Card> extends Iterable<CardType> {

    void addCardToBottom(CardType card);
    CardType takeTopCard();
    CardType takeCardByUuid(UUID uuid);
    void shuffleCards();
    List<CardType> getCards();
    int getSize();

    UUID getUuid();
}
