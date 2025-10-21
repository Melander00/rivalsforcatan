package samuel.player;

import com.fasterxml.jackson.core.type.TypeReference;
import samuel.DirectMessage;
import samuel.Message;
import samuel.MessageType;
import samuel.action.ActionResponse;
import samuel.board.BoardPosition;
import samuel.card.Card;
import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.network.NetworkClient;
import samuel.phase.Phase;
import samuel.player.action.PlayerAction;
import samuel.player.request.RequestCause;
import samuel.request.*;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;
import samuel.util.Pair;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ServerPlayerNetworkHelper implements PlayerNetworkHelper {

    private final NetworkClient client;

    public ServerPlayerNetworkHelper(NetworkClient client) {
        this.client = client;
    }

    @Override
    public int requestInt(int min, int max, RequestCause cause) {
        int res = client.requestData(new Message(MessageType.REQUEST_INT, new Request(cause, new IntRequest(min, max))), Integer.class);
        if(res < min) return min;
        if(res > max) return max;
        return res;
    }

    @Override
    public ResourceBundle requestResource(ResourceBundle bundle, int amount, RequestCause cause) {
        List<ResourceAmount> res = client.requestData(new Message(
                        MessageType.REQUEST_RESOURCE,
                        new Request(cause, new ResourceRequest(bundle, amount))),
                new TypeReference<>() {});

        return ResourceBundle.fromAmounts(res);
    }

    @Override
    public CardStack<PlayableCard> requestCardStack(List<CardStack<PlayableCard>> cardStacks, List<UUID> unselectableStackIds, RequestCause cause) {

        UUID uuid = client.requestData(new Message(MessageType.REQUEST_CARD_STACK, new Request(cause, new CardStackRequest(cardStacks, unselectableStackIds))), UUID.class);
        for(CardStack<PlayableCard> stack : cardStacks) {
            if(stack.getUuid().equals(uuid)) return stack;
        }

        return null;
    }



    @Override
    public BoardPosition requestBoardPosition(List<List<BoardPosition>> positions, RequestCause cause) {

        UUID uuid = client.requestData(new Message(MessageType.REQUEST_BOARD_POSITION, new Request(cause, new BoardPositionRequest(positions))), UUID.class);
        for(List<BoardPosition> row : positions) {
            for(BoardPosition pos : row) {
                if(pos.getUuid().equals(uuid)) {
                    return pos;
                }
            }
        }
        return null;
    }

    @Override
    public <T extends Card> T requestCard(List<T> cards, RequestCause cause) {
        UUID uuid = client.requestData(new Message(
                        MessageType.REQUEST_CARD,
                        new Request(cause, new CardRequest(cards))),
                UUID.class);

        for(T card : cards) {
            if(card.getUuid().equals(uuid)) return card;
        }

        return null;
    }

    @Override
    public boolean requestBoolean(RequestCause cause) {
        Boolean bool = client.requestData(new Message(MessageType.REQUEST_BOOL, new Request(cause, null)), Boolean.class);
        return bool;
    }

    @Override
    public Pair<PlayerAction, BiConsumer<Boolean, String>> requestAction(Phase phase) {
        UUID uuid = UUID.randomUUID();

        PlayerAction action = client.requestData(new Message(MessageType.REQUEST_ACTION, uuid, phase), PlayerAction.class);

        return new Pair<>(action, (success, code) -> {
            client.sendData(new Message(MessageType.RESPONSE, uuid, new ActionResponse(success, code)));
        });
    }

    @Override
    public void directMessage(String msg) {
        client.sendData(new Message(MessageType.DIRECT_MESSAGE, new DirectMessage("Server", msg)));
    }

    @Override
    public void sendMessage(Message msg) {
        client.sendData(msg);
    }

    @Override
    public void addListener(BiConsumer<Message, Player> listener, Player owner) {
//        Player owner = this;
        this.client.addListener(message -> listener.accept(message, owner));
    }
}
