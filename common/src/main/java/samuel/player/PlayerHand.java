package samuel.player;

import samuel.card.Card;
import samuel.card.PlayableCard;

import java.util.List;

public interface PlayerHand extends Iterable<PlayableCard> {

    /**
     * Returns all the cards in the hand.
     * @return
     */
    List<PlayableCard> getCards();

    /**
     * Removes the card from the hand.
     * @param card
     */
    void removeCard(PlayableCard card);

    /**
     * Adds a card to the hand.
     * @param card
     */
    void addCard(PlayableCard card);

    /**
     * Returns the size of the hand.
     * @return
     */
    int getSize();

}
