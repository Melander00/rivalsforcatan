package samuel.game;

import samuel.card.PlayableCard;
import samuel.card.center.CityCard;
import samuel.card.center.RoadCard;
import samuel.card.center.SettlementCard;
import samuel.card.event.EventCard;
import samuel.card.region.RegionCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.deck.Deck;
import samuel.stack.GenericCardStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultStackHandler {
    public static void setupCardDeckAndStacks(Deck deck, GameContext context, int basicCardStacks, int themeCardStacks) {
        StackContainer container = context.getStackContainer();


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

        // We divide the cards into X stacks at random
        List<PlayableCard> cards = new ArrayList<>(deck.getBasicCards());
        Collections.shuffle(cards);
        int cardsPerStack = cards.size() / basicCardStacks;

        // Get sub-lists for each stack
        for(int i = 0; i < basicCardStacks; i++) {
            CardStack<PlayableCard> stack = new GenericCardStack<>();
            List<PlayableCard> sublist = cards.subList(i*cardsPerStack, i*cardsPerStack + cardsPerStack);
            for(PlayableCard card : sublist) {
                stack.addCardToBottom(card);
            }
            container.addToBasicStacks(stack);
        }

        // --- Theme Cards ---

        // We divide the cards into X stacks at random
        if(!deck.getThemeCards().isEmpty()) {
            List<PlayableCard> themeCards = new ArrayList<>(deck.getThemeCards());
            Collections.shuffle(themeCards);
            int themeCardsPerStack = themeCards.size() / themeCardStacks;

            // Get sub-lists for each stack
            for(int i = 0; i < themeCardStacks; i++) {
                CardStack<PlayableCard> stack = new GenericCardStack<>();
                List<PlayableCard> sublist = themeCards.subList(i*themeCardsPerStack, i*themeCardsPerStack + themeCardsPerStack);
                for(PlayableCard card : sublist) {
                    stack.addCardToBottom(card);
                }
                container.addToThemeStacks(stack);
            }
        }
    }
}
