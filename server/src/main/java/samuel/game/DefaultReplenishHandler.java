package samuel.game;

import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.event.player.replenish.PlayerReplenishDiscardEvent;
import samuel.event.player.replenish.PlayerReplenishEvent;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;

public class DefaultReplenishHandler implements ReplenishHandler{
    public void handleReplenish(Player player, GameContext context) {

        // Discard if too many cards
        if(player.isHandFull()) {
            if(player.getHand().getSize() == player.getMaxHandSize()) return;
            context.getEventBus().fireEvent(new PlayerReplenishDiscardEvent(player));

            while(player.getHand().getSize() > player.getMaxHandSize()) {
                PlayableCard toRemove = player.requestCard(player.getHand().getCards(), new RequestCause(RequestCauseEnum.REPLENISH_DISCARD));

                CardStack<PlayableCard> stack = player.requestCardStack(
                        context.getStackContainer().getBasicStacks(),
                        context.getStackContainer().getBasicStacks().stream().filter((s) -> s.getSize() == 0).map(CardStack::getUuid).toList(),
                        new RequestCause(RequestCauseEnum.REPLENISH_DISCARD));

                player.removeCardFromHand(toRemove);
                stack.addCardToBottom(toRemove);
            }

            return;
        }

        context.getEventBus().fireEvent(new PlayerReplenishEvent(player));

        // Replenish while hand is not full.
        while(!player.isHandFull()) {
            CardStack<PlayableCard> stack = player.requestCardStack(
                    context.getStackContainer().getBasicStacks(),
                    context.getStackContainer().getBasicStacks().stream().filter((s) -> s.getSize() == 0).map(CardStack::getUuid).toList(),
                    new RequestCause(RequestCauseEnum.REPLENISH_STACK));
            if(stack.getSize() == 0) continue;
            PlayableCard card = stack.takeTopCard();
            player.addCardToHand(card);
        }
    }
}
