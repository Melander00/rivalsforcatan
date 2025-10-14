package samuel;

import samuel.board.GridBoard;
import samuel.game.Game;
import samuel.game.GameContext;
import samuel.introductory.IntroductoryGame;
import samuel.player.GenericPlayerHand;
import samuel.player.ServerPlayer;
import samuel.network.SocketClient;
import samuel.network.SocketServer;
import samuel.player.Player;
import samuel.response.OpponentResponse;
import samuel.response.StateResponse;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static GameContext context;

    public static void main(String[] args) throws IOException {
        // open socket
        SocketServer socket = new SocketServer(5000);

        // await two players

        Game game = new IntroductoryGame();
        GameServer server = new GameServer(game);

        context = game.getContext();

        List<Player> awaitedPlayers = awaitPlayers(socket, 2);

        server.start(awaitedPlayers);
    }

    private static List<Player> awaitPlayers(SocketServer socket, int nrOfPlayers) throws IOException {
        List<Player> players = new ArrayList<>();
        for(int i = 1; i <= nrOfPlayers; i++) {
            System.out.println("Awaiting Player " + i);
            Socket client = socket.acceptClient();
            System.out.println("Player " + i + " connected.");
            SocketClient socketClient = new SocketClient(client);

            test(socketClient);

            ServerPlayer player = new ServerPlayer(GridBoard.createGridBoard(5, 7), new GenericPlayerHand(), socketClient);

            player.addListener(Main::contextRequestHandler);

            players.add(player);
        }
        return players;
    }

    private static void contextRequestHandler(Message request, Player player) {

        if(context != null && player instanceof ServerPlayer serverPlayer) {

            Object data = switch(request.getType()) {
                case REQUEST_OPPONENT -> {
                    Player opponent = null;
                    for(Player p : context.getPlayers()) {
                        if(!p.equals(player)) {
                            opponent = p;
                            break;
                        }
                    }

                    if(opponent == null) yield null;

                    yield new OpponentResponse(opponent.getPrincipality().getBoardPositions(), opponent.getPoints());
                }
                case REQUEST_STATE -> new StateResponse(context.getActivePlayer().equals(player), context.getPhase().toString());
                default -> null;
            };

            if(data == null) return;

            serverPlayer.sendMessage(new Message(MessageType.RESPONSE, request.getRequestId(), data));

        }
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
