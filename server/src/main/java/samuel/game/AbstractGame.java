package samuel.game;

import samuel.action.ActionResponseType;
import samuel.card.PlaceableCard;
import samuel.card.PlayableCard;
import samuel.card.center.CityCard;
import samuel.card.center.RoadCard;
import samuel.card.center.SettlementCard;
import samuel.card.event.EventCard;
import samuel.card.region.RegionCard;
import samuel.card.stack.CardStack;
import samuel.card.stack.StackContainer;
import samuel.deck.Deck;
import samuel.die.EventDieFace;
import samuel.event.die.EventDieEvent;
import samuel.event.die.ProductionDieEvent;
import samuel.event.player.PlayerTradeEvent;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.action.PlayerAction;
import samuel.player.action.PlayerActionEnum;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;
import samuel.stack.GenericCardStack;
import samuel.util.Pair;

import java.util.*;
import java.util.function.BiConsumer;

public abstract class AbstractGame implements Game {

    private final GameContext context;
    private final Deck deck;

    public AbstractGame(GameContext context, Deck deck) {
        this.context = context;
        this.deck = deck;
    }


    @Override
    public GameContext getContext() {
        return context;
    }

    @Override
    public void initGame() {
        context.setPhase(Phase.INIT);

        // setup card deck and stacks
        setupCardDeckAndStacks();


        // initial draw
        List<CardStack<PlayableCard>> cardStacks = context.getStackContainer().getBasicStacks();
        List<UUID> usedStacks = new ArrayList<>();
        for(Player player : context.getPlayers()) {
            UUID selectedStackId = setupInitialDraw(player, cardStacks, usedStacks);
            usedStacks.add(selectedStackId);
        }

        // setup principality
        for(int i = 0; i < context.getPlayers().size(); i++ ) {
            setupPrincipality(context.getPlayers().get(i), i);
        }

        // finishInit
        setupFinal();
    }

    // todo: move some methods into their own helper class

    public abstract void setupPrincipality(Player player, int playerIndex);

    public void setupCardDeckAndStacks() {
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

        // We divide the cards into X stacks at random
        List<PlayableCard> cards = new ArrayList<>(deck.getBasicCards());
        Collections.shuffle(cards);
        int cardsPerStack = cards.size() / getBasicCardStacks();

        // Get sub-lists for each stack
        for(int i = 0; i < getBasicCardStacks(); i++) {
            CardStack<PlayableCard> stack = new GenericCardStack<>();
            List<PlayableCard> sublist = cards.subList(i*cardsPerStack, i*cardsPerStack + cardsPerStack);
            for(PlayableCard card : sublist) {
                stack.addCardToBottom(card);
            }
            container.addToBasicStacks(stack);
        }

        // --- Theme Cards ---

        // We divide the cards into X stacks at random
        if(deck.getThemeCards() != null) {
            List<PlayableCard> themeCards = new ArrayList<>(deck.getThemeCards());
            Collections.shuffle(themeCards);
            int themeCardsPerStack = themeCards.size() / getThemeCardStacks();

            // Get sub-lists for each stack
            for(int i = 0; i < getThemeCardStacks(); i++) {
                CardStack<PlayableCard> stack = new GenericCardStack<>();
                List<PlayableCard> sublist = themeCards.subList(i*themeCardsPerStack, i*themeCardsPerStack + themeCardsPerStack);
                for(PlayableCard card : sublist) {
                    stack.addCardToBottom(card);
                }
                container.addToThemeStacks(stack);
            }
        }
    }

    public abstract int getBasicCardStacks();
    public abstract int getThemeCardStacks();

    public abstract UUID setupInitialDraw(Player player, List<CardStack<PlayableCard>> cardStacks, List<UUID> usedCardStackIds);

    public abstract void setupFinal();


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
                switchTurn();
            }
        }
        endGame(context.getActivePlayer());
    }

    public void runTurn(Player activePlayer) {
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

    public abstract void switchTurn();

    public abstract void endGame(Player winner);

    @Override
    public void addPlayers(List<Player> players) {
        for(Player player : players) {
            context.addPlayer(player);
            context.getEventBus().register(player);
        }
    }

    public void preDiceRolls(Player activePlayer) {
        boolean hasRolledDice = false;
        while(!hasRolledDice) {
            Pair<PlayerAction, BiConsumer<Boolean, String>> res = activePlayer.requestAction(getContext().getPhase());
            if(res.first().getAction().equals(PlayerActionEnum.ROLL_DICE)) {
                hasRolledDice = true;
                res.second().accept(true, "");
            } else if(res.first().getAction().equals(PlayerActionEnum.PLAY_CARD)) {
                handleCardPlayAction(activePlayer, res.first().getData(), res.second());
            } else {
                res.second().accept(false, ActionResponseType.INVALID_ACTION.toString());
            }
        }
    }

    public void rollAndResolveDice(Player activePlayer) {
        // roll dice

        int res = getContext().rollProductionDie();
        ProductionDieEvent productionEvent = new ProductionDieEvent(activePlayer, res);
        getContext().getEventBus().fireEvent(productionEvent);
        int rollResults = productionEvent.getRollResults();
        getContext().getEventBus().fireEvent(new ProductionDieEvent.Post(rollResults));

        EventDieFace face = getContext().rollEventDice();
        EventDieEvent eventEvent = new EventDieEvent(activePlayer, face);
        getContext().getEventBus().fireEvent(eventEvent);
        EventDieFace eventResults = eventEvent.getRollResults();
        getContext().getEventBus().fireEvent(new EventDieEvent.Post(eventResults));

        if(eventResults.hasPriorityOverProduction()) {
            eventResults.resolve(getContext());
            resolveProduction(rollResults);
        } {
            resolveProduction(rollResults);
            eventResults.resolve(getContext());
        }
    }

    public void resolveProduction(int rollResults) {
        // todo: remove? we use event-based to increase yield
    }

    public void actionPhase(Player activePlayer) {
        boolean hasEndedTurn = false;
        while(!hasEndedTurn) {
            Pair<PlayerAction, BiConsumer<Boolean, String>> res = activePlayer.requestAction(getContext().getPhase());
            if(res.first().getAction().equals(PlayerActionEnum.END_TURN)) {
                hasEndedTurn = true;
                res.second().accept(true, "");
            } else if(res.first().getAction().equals(PlayerActionEnum.PLAY_CARD)) {
                handleCardPlayAction(activePlayer, res.first().getData(), res.second());
            } else if(res.first().getAction().equals(PlayerActionEnum.TRADE)) {
                handleTradeAction(activePlayer, res.first().getData(), res.second());
            } else if(res.first().getAction().equals(PlayerActionEnum.BUILD)) {
                handleBuildAction(activePlayer, res.first().getData(), res.second());
            } else {
                res.second().accept(false, ActionResponseType.INVALID_ACTION.toString());
            }
        }
    }

    public void handleCardPlayAction(Player player, Object data, BiConsumer<Boolean, String> callback) {
        if(data instanceof String uuid) {
            UUID cardToPlay = UUID.fromString(uuid);
            PlayableCard card = player.getCardInHandFromUuid(cardToPlay);
            if(card == null) {
                callback.accept(false, ActionResponseType.CARD_NOT_FOUND.toString());
                return;
            }

            if(card.canPlay(player, getContext())) {
                player.playCard(card, getContext());
                player.removeCardFromHand(card);
                callback.accept(true, "");
                return;
            } else {
                callback.accept(false, ActionResponseType.CARD_CANNOT_BE_PLAYED.toString());
                return;
            }
        } else {
            callback.accept(false, "INVALID_TYPE");
            return;
        }
    }

    public void handleTradeAction(Player player, Object data, BiConsumer<Boolean, String> callback) {



        // ask which resource to pay
        ResourceBundle toPay = player.requestResource(ResourceBundle.oneOfAll(), 1, new RequestCause(RequestCauseEnum.TRADE_PAY));
        ResourceAmount firstToPay = toPay.iterator().next();
        // ask which resource to get
        ResourceBundle toGet = player.requestResource(ResourceBundle.oneOfAll(), 1, new RequestCause(RequestCauseEnum.TRADE_GET));
        ResourceAmount firstToGet = toGet.iterator().next();
        // fire event


        PlayerTradeEvent event = new PlayerTradeEvent(
                player,
                new ResourceAmount(firstToPay.resourceType(), 3), // todo: hard-coded magical number (default trade value 3:1)
                new ResourceAmount(firstToGet.resourceType(), 1));
        getContext().getEventBus().fireEvent(event);
        // get from event how many to pay
        ResourceAmount amountToPay = event.getResourceToPay();
        // check if the player has that amount of resources
        boolean hasEnough = player.hasResources(ResourceBundle.fromAmount(amountToPay));
        // if no - send back that you dont have enough reosurce
        if(!hasEnough) {
            callback.accept(false, ActionResponseType.NOT_ENOUGH_RESOURCES.toString());
            return;
        }
        // ask region to take from x
        player.removeResources(ResourceBundle.fromAmount(amountToPay));
        // give resource
        player.giveResources(ResourceBundle.fromAmount(event.getResourceToGet()));
        // fire post_event
        getContext().getEventBus().fireEvent(new PlayerTradeEvent.Post(player, amountToPay, event.getResourceToGet()));
        // accept callback
        callback.accept(true, "");
    }

    public void handleBuildAction(Player player, Object data, BiConsumer<Boolean, String> callback) {
        if(data instanceof String cardName) {
            StackContainer stacks = getContext().getStackContainer();
            CardStack<PlaceableCard> stack = switch (cardName) {
                case "road" -> stacks.getRoadStack();
                case "settlement" -> stacks.getSettlementStack();
                case "city" -> stacks.getCityStack();
                // todo: Special face-up theme cards
                default -> null;
            };

            if(stack == null) {
                callback.accept(false, ActionResponseType.CARD_NOT_FOUND.toString());
                return;
            }

            PlaceableCard card = stack.peekTopCard();
            boolean canPlay = card.canPlay(player, getContext());

            if(!canPlay) {
                callback.accept(false, ActionResponseType.CARD_CANNOT_BE_PLAYED.toString());
                return;
            }

            player.playCard(card, getContext());
            callback.accept(true, "");
            return;

        } else {
            callback.accept(false, "INVALID_TYPE");
            return;
        }

    }

    // todo: add events
    public void replenishCards(Player activePlayer) {
        while(!activePlayer.isHandFull()) {
            CardStack<PlayableCard> stack = activePlayer.requestCardStack(
                    getContext().getStackContainer().getBasicStacks(),
                    getContext().getStackContainer().getBasicStacks().stream().filter((s) -> s.getSize() == 0).map(CardStack::getUuid).toList(),
                    new RequestCause(RequestCauseEnum.REPLENISH_STACK));
            if(stack.getSize() == 0) {
                // todo:
            }
            PlayableCard card = stack.takeTopCard();
            activePlayer.addCardToHand(card);
        }
    }

    // todo: add events
    public void exchangeCards(Player activePlayer) {
        boolean shouldExchange = activePlayer.requestBoolean(new RequestCause(RequestCauseEnum.EXCHANGE));
        if(shouldExchange) {

            // Choose which card to discard and where
            PlayableCard card = activePlayer.requestCard(activePlayer.getHand().getCards(), new RequestCause(RequestCauseEnum.EXCHANGE_DISCARD_CARD));
            CardStack<PlayableCard> discardStack = activePlayer.requestCardStack(getContext().getStackContainer().getBasicStacks(), List.of(), new RequestCause(RequestCauseEnum.EXCHANGE_DISCARD_STACK));
            discardStack.addCardToBottom(card);
            activePlayer.removeCardFromHand(card);

            // Choose which stack to take from
            CardStack<PlayableCard> takeStack = activePlayer.requestCardStack(
                    getContext().getStackContainer().getBasicStacks(),
                    getContext().getStackContainer().getBasicStacks().stream().filter((s) -> s.getSize() == 0).map(CardStack::getUuid).toList(),
                    new RequestCause(RequestCauseEnum.EXCHANGE_TAKE_STACK));

            boolean searchStack = activePlayer.requestBoolean(new RequestCause(RequestCauseEnum.EXCHANGE_SEARCH));
            if(searchStack) {
                // todo: parish hall, event?

                // ask which resources to pay with
                ResourceBundle toPayWith = activePlayer.requestResource(activePlayer.getResources(), 2, new RequestCause(RequestCauseEnum.EXCHANGE_SEARCH));
                // pay resources
                activePlayer.removeResources(toPayWith);
                // ask which card in stack
                PlayableCard c = activePlayer.requestCard(takeStack.getCards(), new RequestCause(RequestCauseEnum.EXCHANGE_SEARCH));
                // add that card to player hand
                PlayableCard removedCard = takeStack.takeCardByUuid(c.getUuid());
                activePlayer.addCardToHand(removedCard);
                // shuffle stack
                takeStack.shuffleCards();
            } else {
                activePlayer.addCardToHand(takeStack.takeTopCard());
            }
        }
    }
}
