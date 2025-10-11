package samuel.game;

import samuel.card.Card;
import samuel.card.center.CityCard;
import samuel.card.center.RoadCard;
import samuel.card.center.SettlementCard;
import samuel.card.event.EventCard;
import samuel.card.region.RegionCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.deck.Deck;
import samuel.deck.IntroductoryDeck;
import samuel.player.Player;
import samuel.principalities.IntrodoctoryPrincipality;
import samuel.stack.GenericCardStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class IntroductoryGame extends AbstractGame {

    public IntroductoryGame() {
        super(new IntroductoryGameContext());
    }

    @Override
    void setupPrincipality(Player player, int playerIndex) {
        IntrodoctoryPrincipality.setupPrincipality(player.getPrincipality());
    }

    @Override
    void setupCardDeckAndStacks() {
        Deck deck = new IntroductoryDeck();

        StackContainer container = getContext().getStackContainer();


        // --- Center Cards ---
        for(int i = 0; i < deck.getAmountOfRoadCards(); i++) {
            container.getRoadStack().addCardToBottom(new RoadCard());
        }

        for(int i = 0; i < deck.getAmountOfSettlementCards(); i++) {
            container.getSettlementStack().addCardToBottom(new SettlementCard());
        }

        for(int i = 0; i < deck.getAmountOfCityCards(); i++) {
            container.getCityStack().addCardToBottom(new CityCard());
        }


        // --- Region Cards ---
        for(RegionCard card : deck.getRegionCards()) {
            container.getRegionStack().addCardToBottom(card);
        }
        container.getRegionStack().shuffleCards();


        // --- Event Cards ---
        for(EventCard card : deck.getEventCards()) {
            container.getEventStack().addCardToBottom(card);
        }
        container.getEventStack().shuffleCards();

        // --- Basic Cards ---

        // We divide the cards into four stacks at random
        List<Card> cards = new ArrayList<>(deck.getBasicCards());
        Collections.shuffle(cards);
        int cardsPerStack = cards.size() / 4;

        /*
         0 1 2 3 4 5 6 7

         cps = 8/4 = 2
         loop:
           i = 0:
             sublist(0, 1) => 0 1
           i = 2:
             sublist(2, 3) => 2 3

         */

        // Get sublists for each stack
        for(int i = 0; i < 4; i += cardsPerStack) {
            CardStack<Card> stack = new GenericCardStack<>();
            List<Card> sublist = cards.subList(i, i + cardsPerStack - 1);
            for(Card card : sublist) {
                stack.addCardToBottom(card);
            }
            container.addToBasicStacks(stack);
        }



    }




    @Override
    UUID setupInitialDraw(Player player, List<CardStack<Card>> cardStacks, List<UUID> usedCardStackIds) {
        return null;
    }

    @Override
    void setupFinal() {
        for(Player player : getContext().getPlayers()) {
            player.directMessage("Game setup complete.");
        }
    }

    @Override
    void runTurn(Player activePlayer) {
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

    @Override
    void switchTurn() {
        getContext().switchTurn();
    }

    @Override
    void endGame(Player winner) {
        winner.directMessage("You won!");
        System.out.println("The game has ended");
    }
}
