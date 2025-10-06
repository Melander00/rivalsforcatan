package samuel;

import samuel.board.IntroductoryPrincipality;
import samuel.eventmanager.GenericEventBus;
import samuel.game.GameContext;
import samuel.game.IntroductoryGameContext;
import samuel.game.ServerPlayer;
import samuel.network.SocketServer;
import samuel.player.Player;
import samuel.ui.Console;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {


    public static void main(String[] args) throws IOException {
        (new ServerMain()).init();
    }

    private final IntroductoryGameContext context;

    public ServerMain() {
        this.context = new IntroductoryGameContext();
    }

    private void init() throws IOException {
        // For primitiveness, this method is composite of multiple helpers.

        // open socket
        SocketServer socket = new SocketServer(5000, new Console());

        // await two players
        Socket client1 = socket.acceptClient();
        ServerPlayer player1 = new ServerPlayer(new IntroductoryPrincipality());
        ServerPlayer player2 = new ServerPlayer(new IntroductoryPrincipality());

        // randomize RED/BLUE player.
        List<Player> players = randomizeOrder(List.of(player1, player2));
        for(Player player : players) {
            this.context.addPlayer(player);
        }

        // initiate principalities


        // load introductory cards


        // Choose initial draw stack
        chooseInitialDrawStack(this.context);

        // run game
        run(this.context);
    }

    private List<Player> randomizeOrder(List<Player> players) {
        List<Player> randomized = new ArrayList<>();

        for(int i = 0; i < players.size(); i++) {
            randomized.add(players.get((int)(Math.random() * i)));
        }

        return randomized;
    }

    private void chooseInitialDrawStack(GameContext context) {
        for(Player player : context.getPlayers()) {
            player.requestCardStack(List.of()); // todo
        }
    }

    private void run(GameContext context) {

    }
}