package samuel;

import samuel.board.GridBoard;
import samuel.condition.VictoryCondition;
import samuel.game.Game;
import samuel.game.GameContext;
import samuel.introductory.IntroductoryGame;
import samuel.network.NetworkClient;
import samuel.network.NetworkServer;
import samuel.player.GenericPlayerHand;
import samuel.player.ServerPlayer;
import samuel.network.SocketClient;
import samuel.network.SocketServer;
import samuel.player.Player;
import samuel.player.ServerPlayerNetworkHelper;
import samuel.point.PointBundle;
import samuel.point.points.VictoryPoint;
import samuel.response.OpponentResponse;
import samuel.response.StateResponse;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static GameContext context;

    public static void main(String[] args) throws IOException {
        // Network Server
        SocketServer socket = new SocketServer(5000);

        // Game Type
        Game game = new IntroductoryGame();
        GameServer server = new GameServer(game);

        // Setup
        context = game.getContext();
        List<Player> awaitedPlayers = awaitPlayers(socket, 2);
        server.start(awaitedPlayers);
    }






    private static List<Player> awaitPlayers(NetworkServer socket, int nrOfPlayers) {
        List<Player> players = new ArrayList<>();
        for(int i = 1; i <= nrOfPlayers; i++) {
            System.out.println("Awaiting Player " + i);
            NetworkClient socketClient = socket.acceptClient();
            System.out.println("Player " + i + " connected.");

            ServerPlayer player = new ServerPlayer(
                    GridBoard.createGridBoard(5, 7),
                    new GenericPlayerHand(),
                    new ServerPlayerNetworkHelper(socketClient));

            player.addListener(Main::contextRequestHandler);

            players.add(player);
        }
        return players;
    }

    // This isn't the correct place for this but i dont have time ATM to figure it out lmao.
    private static void contextRequestHandler(Message request, Player player) {
        if(request == null || request.getType() == null) return;

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

                    PointBundle points = opponent.getPoints();
                    int victory = context.getVictoryPoints(opponent);
                    int diff = victory - points.getAmount(VictoryPoint.class);
                    points.addPoint(VictoryPoint.class, diff);

                    yield new OpponentResponse(opponent.getPrincipality().getBoardPositions(), points);
                }
//                case REQUEST_STATE -> context.getPlayers();
                case REQUEST_STATE -> new StateResponse(context.getActivePlayer().equals(player), context.getPhase().toString());
                case REQUEST_STACKS -> context.getStackContainer();
                case REQUEST_POINTS -> {
                    PointBundle points = player.getPoints();
                    int victory = context.getVictoryPoints(player);
                    int diff = victory - points.getAmount(VictoryPoint.class);
                    points.addPoint(VictoryPoint.class, diff);
                    yield points;
                }
                default -> null;
            };

            if(data == null) return;


            serverPlayer.sendMessage(new Message(MessageType.RESPONSE, request.getRequestId(), data));

        }
    }
}
