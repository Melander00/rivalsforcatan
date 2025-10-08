package samuel;

import samuel.game.Game;
import samuel.game.GameContext;
import samuel.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameServer {




    private final Game game;

    public GameServer(Game game) {
        this.game = game;
    }

    public void start(List<Player> players) throws IOException {
        // add players
        this.game.addPlayers(players);

        // initialize game
        this.game.initGame();

        // run game
        this.game.run();
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