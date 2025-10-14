package samuel.introductory;

import samuel.game.AbstractGame;
import samuel.player.Player;

public class IntroductoryGame extends AbstractGame {

    public IntroductoryGame() {
        super(
                new IntroductoryGameContext(),
                new IntroductoryDeck()
        );
    }

    @Override
    public void setupPrincipality(Player player, int playerIndex) {
        IntrodoctoryPrincipality.setupPrincipality(player, playerIndex, getContext());
    }

    @Override
    public int getBasicCardStacks() {
        return 4;
    }

    @Override
    public int getThemeCardStacks() {
        return 0;
    }

    @Override
    public void setupFinal() {
        for(Player player : getContext().getPlayers()) {
            player.directMessage("Game setup complete.");
        }
    }

    @Override
    public void runTurn(Player activePlayer) {
        activePlayer.directMessage("Your turn");

        super.runTurn(activePlayer);
    }

    @Override
    public void switchTurn() {
        getContext().switchTurn();
    }

    @Override
    public void endGame(Player winner) {
        winner.directMessage("You won!");
        System.out.println("The game has ended");
    }
}
