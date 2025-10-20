package samuel.card.stack;

import samuel.card.Card;

import java.util.List;
import java.util.UUID;

public interface CardStack<CardType extends Card> extends Iterable<CardType> {

    /**
     * Adds the card to the bottom of the stack.
     * @param card
     */
    void addCardToBottom(CardType card);

    /**
     * Returns the top card without removing it from the stack.
     * @return
     */
    CardType peekTopCard();

    /**
     * Removes and returns the top card.
     * @return
     */
    CardType takeTopCard();

    /**
     * Looks through the stack to find the one with matching UUID, removes and returns it.
     * @param uuid
     * @return
     */
    CardType takeCardByUuid(UUID uuid);

    /**
     * Removes and returns a specific card from the stack.
     * @param card
     * @return
     */
    CardType removeCard(CardType card);

    /**
     * Shuffles the stack in-place.
     */
    void shuffleCards();

    /**
     * Returns a list of all the cards currently in the stack.
     * @return
     */
    List<CardType> getCards();

    /**
     * Returns how many cards are in the stack.
     * @return
     */
    int getSize();

    /**
     * Overwrites the internal list of cards with the new one.
     * @param cards
     */
    void setCards(List<CardType> cards);

    /**
     * Returns a unique identifier for this stack.
     * @return
     */
    UUID getUuid();
}
