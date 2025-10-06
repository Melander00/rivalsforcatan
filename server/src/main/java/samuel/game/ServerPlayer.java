package samuel.game;

import samuel.board.Board;
import samuel.card.stack.CardStack;
import samuel.effect.Effect;
import samuel.network.SocketClient;
import samuel.player.Player;
import samuel.resource.ResourceBundle;

import java.net.Socket;
import java.util.List;

public class ServerPlayer implements Player {

    private final Board principality;
    private final SocketClient client;

    public ServerPlayer(Board principality, SocketClient client) {
        this.principality = principality;
        this.client = client;
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
        return 0;
    }

    @Override
    public int requestInt(int min, int max) {
        return 0;
    }

    @Override
    public ResourceBundle requestResource(ResourceBundle bundle, int amount) {
        return null;
    }

    @Override
    public CardStack<?> requestCardStack(List<CardStack<?>> cardStacks) {
        return null;
    }
}
