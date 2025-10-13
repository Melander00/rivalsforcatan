package samuel.game;

import samuel.card.Card;
import samuel.card.PlayableCard;
import samuel.card.center.CityCard;
import samuel.card.center.RoadCard;
import samuel.card.center.SettlementCard;
import samuel.card.event.EventCard;
import samuel.card.region.RegionCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.deck.Deck;
import samuel.deck.IntroductoryDeck;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.request.RequestCause;
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
        List<PlayableCard> cards = new ArrayList<>(deck.getBasicCards());
        Collections.shuffle(cards);
        int cardsPerStack = cards.size() / 4;

        /*
         0 1 2 3 4 5 6 7 8 9 10 11

         cps = 12/4 = 3
         loop:
           i = 0:
             sublist(0, 2) => 0 1 2
           i = 1:
             sublist(3, 5) => 3 4 5
           i = 2:
             sublist(6, 8) => 6 7 8
           i = 3:
             sublist(9, 11) => 9 10 11
         */

        // Get sub-lists for each stack
        for(int i = 0; i < 4; i++) {
            CardStack<PlayableCard> stack = new GenericCardStack<>();
            List<PlayableCard> sublist = cards.subList(i*cardsPerStack, i*cardsPerStack + cardsPerStack);
            for(PlayableCard card : sublist) {
                stack.addCardToBottom(card);
            }
            container.addToBasicStacks(stack);
        }



    }




    @Override
    UUID setupInitialDraw(Player player, List<CardStack<PlayableCard>> cardStacks, List<UUID> usedCardStackIds) {
        CardStack<PlayableCard> stack = player.requestCardStack(cardStacks, usedCardStackIds, RequestCause.INITIAL_DRAW);
        if(usedCardStackIds.contains(stack.getUuid())) {
            // Bad value from client, default to the next stack available
            for(CardStack<PlayableCard> cardStack : cardStacks) {
                if(!usedCardStackIds.contains(cardStack.getUuid())){
                    stack = cardStack;
                    break;
                }
            }
        }

        for(int i = 0; i < 3; i++) {
            PlayableCard card = stack.takeTopCard();
            player.addCardToHand(card);
        }

        return stack.getUuid();
    }

    @Override
    void setupFinal() {
        for(Player player : getContext().getPlayers()) {
            player.directMessage("Game setup complete.");
        }
    }

    @Override
    void runTurn(Player activePlayer) {
        activePlayer.directMessage("Your turn");

        getContext().setPhase(Phase.DICE_ROLL);
        preDiceRolls(activePlayer);
        rollAndResolveDice(activePlayer);

        getContext().setPhase(Phase.ACTION);
        actionPhase(activePlayer);

        getContext().setPhase(Phase.REPLENISH);
        replenishCards(activePlayer);

        getContext().setPhase(Phase.EXCHANGE);
        exchangeCards(activePlayer);
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
