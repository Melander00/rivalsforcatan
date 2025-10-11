package samuel.player;

import samuel.DirectMessage;
import samuel.Message;
import samuel.MessageType;
import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.PlaceableCard;
import samuel.card.PointHolder;
import samuel.card.stack.CardStack;
import samuel.effect.Effect;
import samuel.network.SocketClient;
import samuel.point.Point;
import samuel.point.PointBundle;
import samuel.request.CardStackRequest;
import samuel.request.IntRequest;
import samuel.resource.ResourceBundle;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class ServerPlayer implements Player {

    private final Board principality;
    private final SocketClient client;

    public ServerPlayer(Board principality, SocketClient client) {
        this.principality = principality;
        this.client = client;
        client.addListener(this::clientRequestHandler);
    }




    @Override
    public Board getPrincipality() {
        return this.principality;
    }

    @Override
    public void giveEffect(Effect effect) {

    }

    @Override
    public int requestInt() {
        return this.requestInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public int requestInt(int min, int max) {
        try {
            int res = client.requestData(new Message(MessageType.REQUEST_INT, new IntRequest(min, max)), Integer.class);
            if(res < min) return min;
            if(res > max) return max;
            return res;
        } catch (IOException | InterruptedException exception) {
            // handle the exception
            return 0;
        }
    }

    @Override
    public ResourceBundle requestResource(ResourceBundle bundle, int amount) {
        return null;
    }

    @Override
    public CardStack<?> requestCardStack(List<CardStack<?>> cardStacks) {

        try {
            int index = client.requestData(new Message(MessageType.REQUEST_CARD_STACK, new CardStackRequest()), Integer.class);

        } catch (IOException | InterruptedException exception) {

        }

        return null;
    }

    @Override
    public BoardPosition requestBoardPosition() {
        return null;
    }

    @Override
    public <T extends Point> int getPoints(Class<T> pointClass) {
        int points = 0;

        for(BoardPosition position : this.principality) {
            PlaceableCard card = position.getCard();
            if(card == null) continue;

            if(card instanceof PointHolder holder) {
                PointBundle bundle = holder.getPoints();
                points += bundle.getAmount(pointClass);
            }
        }
        return points;

    }

    @Override
    public void directMessage(String msg) {
        sendMessage(new Message(MessageType.DIRECT_MESSAGE, new DirectMessage("server", msg)));
    }

    private void sendMessage(Message msg) {
        try {
            client.sendData(msg);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void clientRequestHandler(Message request) {
        System.out.println("Received request " + request.getType().toString() + " with id " + request.getRequestId().toString());

        switch(request.getType()) {
            case REQUEST_BOARD -> sendMessage(new Message(MessageType.RESPONSE, request.getRequestId(), principality));
        }
    }
}
