package samuel.game;

import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;

public class DefaultReplenishHandler implements ReplenishHandler{
    // todo: add events
    public void handleReplenish(Player activePlayer, GameContext context) {
        // todo: discard if player hand is overfull
        while(!activePlayer.isHandFull()) {
            CardStack<PlayableCard> stack = activePlayer.requestCardStack(
                    context.getStackContainer().getBasicStacks(),
                    context.getStackContainer().getBasicStacks().stream().filter((s) -> s.getSize() == 0).map(CardStack::getUuid).toList(),
                    new RequestCause(RequestCauseEnum.REPLENISH_STACK));
            if(stack.getSize() == 0) {
                // todo:
            }
            PlayableCard card = stack.takeTopCard();
            activePlayer.addCardToHand(card);
        }
    }
}
