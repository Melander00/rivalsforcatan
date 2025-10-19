package samuel.game;

import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.event.player.exchange.PlayerExchangeEvent;
import samuel.event.player.exchange.PlayerExchangeSearchEvent;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.resource.ResourceBundle;

import java.util.List;

public class DefaultExchangeHandler implements ExchangeHandler {

    public void handleExchange(Player activePlayer, GameContext context) {
        boolean shouldExchange = activePlayer.requestBoolean(new RequestCause(RequestCauseEnum.EXCHANGE));
        if(shouldExchange) {
            context.getEventBus().fireEvent(new PlayerExchangeEvent(activePlayer));

            // Choose which card to discard and where
            PlayableCard card = activePlayer.requestCard(activePlayer.getHand().getCards(), new RequestCause(RequestCauseEnum.EXCHANGE_DISCARD_CARD));
            CardStack<PlayableCard> discardStack = activePlayer.requestCardStack(context.getStackContainer().getBasicStacks(), List.of(), new RequestCause(RequestCauseEnum.EXCHANGE_DISCARD_STACK));
            discardStack.addCardToBottom(card);
            activePlayer.removeCardFromHand(card);

            // Choose which stack to take from
            CardStack<PlayableCard> takeStack = activePlayer.requestCardStack(
                    context.getStackContainer().getBasicStacks(),
                    context.getStackContainer().getBasicStacks().stream().filter((s) -> s.getSize() == 0).map(CardStack::getUuid).toList(),
                    new RequestCause(RequestCauseEnum.EXCHANGE_TAKE_STACK));

            boolean searchStack = activePlayer.requestBoolean(new RequestCause(RequestCauseEnum.EXCHANGE_SEARCH));
            if(searchStack) {

                PlayerExchangeSearchEvent event = new PlayerExchangeSearchEvent(activePlayer, 2);
                context.getEventBus().fireEvent(event);

                // ask which resources to pay with
                ResourceBundle toPayWith = activePlayer.requestResource(activePlayer.getResources(), event.getResourcesToPay(), new RequestCause(RequestCauseEnum.EXCHANGE_SEARCH));
                // pay resources
                activePlayer.removeResources(toPayWith);
                // ask which card in stack
                PlayableCard c = activePlayer.requestCard(takeStack.getCards(), new RequestCause(RequestCauseEnum.EXCHANGE_SEARCH));
                // add that card to player hand
                PlayableCard removedCard = takeStack.removeCard(c);
                activePlayer.addCardToHand(removedCard);
                // shuffle stack
                takeStack.shuffleCards();
            } else {
                PlayableCard toGet = takeStack.takeTopCard();
                activePlayer.addCardToHand(toGet);
            }
        }
    }
}
