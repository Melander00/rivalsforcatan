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

    public void start(List<Player> players) {
        // add players
        this.game.addPlayers(players);

        // initialize game
        this.game.initGame();

        // run game
        this.game.run();
    }
}