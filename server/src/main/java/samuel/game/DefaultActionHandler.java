package samuel.game;

import samuel.action.ActionResponseType;
import samuel.card.PlaceableCard;
import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.event.player.PlayerTradeEvent;
import samuel.player.Player;
import samuel.player.action.PlayerAction;
import samuel.player.action.PlayerActionEnum;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;
import samuel.util.Pair;

import java.util.UUID;
import java.util.function.BiConsumer;

public class DefaultActionHandler implements ActionHandler {

    public void handleActionPhase(Player activePlayer, GameContext context) {
        boolean hasEndedTurn = false;
        while(!hasEndedTurn) {
            Pair<PlayerAction, BiConsumer<Boolean, String>> res = activePlayer.requestAction(context.getPhase());
            if(res.first().getAction().equals(PlayerActionEnum.END_TURN)) {
                hasEndedTurn = true;
                res.second().accept(true, "");

            } else if(res.first().getAction().equals(PlayerActionEnum.PLAY_CARD)) {
                handlePlayCardAction(activePlayer, context, res.first().getData(), res.second());

            } else if(res.first().getAction().equals(PlayerActionEnum.TRADE)) {
                handleTradeAction(activePlayer, context, res.first().getData(), res.second());

            } else if(res.first().getAction().equals(PlayerActionEnum.BUILD)) {
                handleBuildAction(activePlayer, context, res.first().getData(), res.second());

            } else {
                res.second().accept(false, ActionResponseType.INVALID_ACTION.toString());

            }
        }
    }

    public void handlePlayCardAction(Player player, GameContext context, Object data, BiConsumer<Boolean, String> callback) {
        if(data instanceof String uuid) {
            UUID cardToPlay = UUID.fromString(uuid);
            PlayableCard card = player.getCardInHandFromUuid(cardToPlay);
            if(card == null) {
                callback.accept(false, ActionResponseType.CARD_NOT_FOUND.toString());
                return;
            }

            if(card.canPlay(player, context)) {
                player.playCard(card, context);
                player.removeCardFromHand(card);
                callback.accept(true, "");
                return;
            } else {
                callback.accept(false, ActionResponseType.CARD_CANNOT_BE_PLAYED.toString());
                return;
            }
        } else {
            callback.accept(false, "INVALID_TYPE");
            return;
        }
    }

    public void handleTradeAction(Player player, GameContext context, Object data, BiConsumer<Boolean, String> callback) {
        // ask which resource to pay
        ResourceBundle toPay = player.requestResource(ResourceBundle.oneOfAll(), 1, new RequestCause(RequestCauseEnum.TRADE_PAY));
        ResourceAmount firstToPay = toPay.iterator().next();
        // ask which resource to get
        ResourceBundle toGet = player.requestResource(ResourceBundle.oneOfAll(), 1, new RequestCause(RequestCauseEnum.TRADE_GET));
        ResourceAmount firstToGet = toGet.iterator().next();
        // fire event


        PlayerTradeEvent event = new PlayerTradeEvent(
                player,
                new ResourceAmount(firstToPay.resourceType(), 3),
                new ResourceAmount(firstToGet.resourceType(), 1));
        context.getEventBus().fireEvent(event);
        // get from event how many to pay
        ResourceAmount amountToPay = event.getResourceToPay();
        // check if the player has that amount of resources
        boolean hasEnough = player.hasResources(ResourceBundle.fromAmount(amountToPay));
        // if no - send back that you dont have enough reosurce
        if(!hasEnough) {
            callback.accept(false, ActionResponseType.NOT_ENOUGH_RESOURCES.toString());
            return;
        }
        // ask region to take from x
        player.removeResources(ResourceBundle.fromAmount(amountToPay));
        // give resource
        player.giveResources(ResourceBundle.fromAmount(event.getResourceToGet()));
        // fire post_event
        context.getEventBus().fireEvent(new PlayerTradeEvent.Post(player, amountToPay, event.getResourceToGet()));
        // accept callback
        callback.accept(true, "");
    }

    public void handleBuildAction(Player player, GameContext context, Object data, BiConsumer<Boolean, String> callback) {
        if(data instanceof String cardName) {
            StackContainer stacks = context.getStackContainer();
            CardStack<PlaceableCard> stack = switch (cardName) {
                case "road" -> stacks.getRoadStack();
                case "settlement" -> stacks.getSettlementStack();
                case "city" -> stacks.getCityStack();
                // Special face-up theme cards
                default -> null;
            };

            if(stack == null) {
                callback.accept(false, ActionResponseType.CARD_NOT_FOUND.toString());
                return;
            }

            PlaceableCard card = stack.peekTopCard();
            boolean canPlay = card.canPlay(player, context);

            if(!canPlay) {
                callback.accept(false, ActionResponseType.CARD_CANNOT_BE_PLAYED.toString());
                return;
            }

            player.playCard(card, context);
            callback.accept(true, "");
            return;

        } else {
            callback.accept(false, "INVALID_TYPE");
            return;
        }

    }
}
