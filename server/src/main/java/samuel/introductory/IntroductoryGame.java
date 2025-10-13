package samuel.introductory;

import samuel.card.PlayableCard;
import samuel.card.center.CityCard;
import samuel.card.center.RoadCard;
import samuel.card.center.SettlementCard;
import samuel.card.event.EventCard;
import samuel.card.region.RegionCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.deck.Deck;
import samuel.game.AbstractGame;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.stack.GenericCardStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class IntroductoryGame extends AbstractGame {

    public IntroductoryGame() {
        super(
                new IntroductoryGameContext(),
                new IntroductoryDeck()
        );
    }

    @Override
    public void setupPrincipality(Player player, int playerIndex) {
        IntrodoctoryPrincipality.setupPrincipality(player, getContext());
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
