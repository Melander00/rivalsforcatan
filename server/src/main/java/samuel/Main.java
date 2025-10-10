package samuel;

import samuel.board.BoardPosition;
import samuel.board.GridBoard;
import samuel.card.region.ForestRegionCard;
import samuel.game.IntroductoryGame;
import samuel.player.ServerPlayer;
import samuel.network.SocketClient;
import samuel.network.SocketServer;
import samuel.player.Player;
import samuel.point.PointBundle;
import samuel.point.points.VictoryPoint;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.OreResource;
import samuel.resource.resources.TimberResource;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        // open socket
        SocketServer socket = new SocketServer(5000);

        // await two players
        List<Player> awaitedPlayers = awaitPlayers(socket, 1);

        (new GameServer(new IntroductoryGame())).start(awaitedPlayers);
    }

    private static List<Player> awaitPlayers(SocketServer socket, int nrOfPlayers) throws IOException {
        List<Player> players = new ArrayList<>();
        for(int i = 1; i <= nrOfPlayers; i++) {
            System.out.println("Awaiting Player " + i);
            Socket client = socket.acceptClient();
            System.out.println("Player " + i + " connected.");
            SocketClient socketClient = new SocketClient(client);

            test(socketClient);

            ServerPlayer player = new ServerPlayer(GridBoard.createGridBoard(), socketClient);
            players.add(player);
        }
        return players;
    }

    private static void test(SocketClient client) throws IOException {
//        client.sendData(new Message(MessageType.GENERIC, new ForestRegionCard(0)));
//        UUID uuid = client.requestData(new Message(MessageType.REQUEST_BOARD_POSITION, null), UUID.class);
//        System.out.println(uuid);
//
//        GridBoard board = GridBoard.createGridBoard();
//        BoardPosition pos = board.getPositionOnGrid(2, 2);
//        board.place(new ForestRegionCard(1), pos);
//        client.sendData(new Message(MessageType.GENERIC, board));
//
//        ResourceBundle bundle = new ResourceBundle();
//        bundle.addResource(TimberResource.class, 2);
//        bundle.addResource(OreResource.class, 2);
//        client.sendData(new Message(MessageType.GENERIC, bundle));
//
//        PointBundle pointBundle = new PointBundle();
//        pointBundle.addPoint(VictoryPoint.class, 2);
//        client.sendData(new Message(MessageType.GENERIC, pointBundle));

    }
}
