package samuel.game;

import samuel.player.Player;

import java.util.List;

public interface Game {
    /**
     * Initializes the game
     */
    void initGame();

    /**
     * The game loop
     */
    void run();

    /**
     * Add all the players to the game
     */
    void addPlayers(List<Player> players);

    /**
     * Returns the game context for this game.
     * @return
     */
    GameContext getContext();
}
