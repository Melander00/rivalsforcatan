package samuel.game;

import samuel.player.Player;
import samuel.principalities.IntrodoctoryPrincipality;

import java.util.List;

public class IntroductoryGame implements Game {

    private final IntroductoryGameContext context = new IntroductoryGameContext();

    @Override
    public void initGame() {
        // setup principality
        for(Player player : context.getPlayers()) {
            IntrodoctoryPrincipality.setupPrincipality(player.getPrincipality());
        }

        // setup card deck


        // initial draw


        // Send init message to players
        System.out.println("Setup complete.");
        for(Player player : context.getPlayers()) {
            player.directMessage("Game has been set up.");
        }
    }


    @Override
    public void run() {
        boolean finished = false;
        while(!finished) {
            // run each turn
            runTurn(context.getActivePlayer());

            // check victory condition
            boolean hasWon = context.hasWon(context.getActivePlayer());
            if(hasWon) {
                finished = true;
            } else {
                // set next player as the active player
                // we don't want to switch if the game is over
                context.switchTurn();
            }
        }
        endGame(context.getActivePlayer());
    }

    private void runTurn(Player activePlayer) {
        // (optional?) ask the players if they want to play a card before the run
        // will be needed to allow BrigitteTheWiseWoman to be used correctly
        // she is played before. alternatively let the player who rolls decide when to roll or if to play a card

        // roll dice

        // resolve dice
            // if brigand, resolve event first
            // else, resolve production -> event

        // action phase

        // replenish

        // exchange
    }

    private void endGame(Player winner) {
        System.out.println("The game has ended");
    }

    @Override
    public void addPlayers(List<Player> players) {
        for(Player player : players) {
            context.addPlayer(player);
        }
    }
}
