package samuel.player;

import samuel.card.Card;
import samuel.card.PlayableCard;

import java.util.List;

public interface PlayerHand extends Iterable<PlayableCard> {

    List<PlayableCard> getCards();

    void removeCard(PlayableCard card);

    void addCard(PlayableCard card);

    int getSize();

}
