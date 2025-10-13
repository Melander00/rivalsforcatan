package samuel.game;

import samuel.action.ActionResponseType;
import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.die.EventDieFace;
import samuel.event.die.EventDieEvent;
import samuel.event.die.ProductionDieEvent;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.action.PlayerAction;
import samuel.player.action.PlayerActionEnum;
import samuel.player.request.RequestCause;
import samuel.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public abstract class AbstractGame implements Game {

    private final GameContext context;

    public AbstractGame(GameContext context) {
        this.context = context;
    }

    @Override
    public GameContext getContext() {
        return context;
    }

    @Override
    public void initGame() {
        context.setPhase(Phase.INIT);

        // setup principality
        for(int i = 0; i < context.getPlayers().size(); i++ ) {
            setupPrincipality(context.getPlayers().get(i), i);
        }

        // setup card deck and stacks
        setupCardDeckAndStacks();

        // initial draw
        List<CardStack<PlayableCard>> cardStacks = context.getStackContainer().getBasicStacks();
        List<UUID> usedStacks = new ArrayList<>();
        for(Player player : context.getPlayers()) {
            UUID selectedStackId = setupInitialDraw(player, cardStacks, usedStacks);
            usedStacks.add(selectedStackId);
        }

        // finishInit
        setupFinal();
    }

    abstract void setupPrincipality(Player player, int playerIndex);

    abstract void setupCardDeckAndStacks();

    abstract UUID setupInitialDraw(Player player, List<CardStack<PlayableCard>> cardStacks, List<UUID> usedCardStackIds);

    abstract void setupFinal();


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

    abstract void runTurn(Player activePlayer);

    abstract void switchTurn();

    abstract void endGame(Player winner);

    @Override
    public void addPlayers(List<Player> players) {
        for(Player player : players) {
            context.addPlayer(player);
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

    private void resolveProduction(int rollResults) {
        System.out.println("Production has rolled " + rollResults);
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

            if(card.canPlay(getContext())) {
                card.play(player, getContext());
                player.removeCardFromHand(card);
                callback.accept(true, "");
            } else {
                callback.accept(false, ActionResponseType.CARD_CANNOT_BE_PLAYED.toString());
            }
        } else {
            callback.accept(false, "INVALID_UUID");
        }
    }

    public void handleTradeAction(Player player, Object data, BiConsumer<Boolean, String> callback) {

    }

    // todo: add events
    public void replenishCards(Player activePlayer) {
        while(!activePlayer.isHandFull()) {
            CardStack<PlayableCard> stack = activePlayer.requestCardStack(
                    getContext().getStackContainer().getBasicStacks(),
                    getContext().getStackContainer().getBasicStacks().stream().filter((s) -> s.getSize() == 0).map(CardStack::getUuid).toList(),
                    RequestCause.REPLENISH_STACK);
            if(stack.getSize() == 0) {
                // todo:
            }
            PlayableCard card = stack.takeTopCard();
            activePlayer.addCardToHand(card);
        }
    }

    // todo: add events
    public void exchangeCards(Player activePlayer) {
        boolean shouldExchange = activePlayer.requestBoolean(RequestCause.EXCHANGE);
        if(shouldExchange) {

            // Choose which card to discard and where
            PlayableCard card = activePlayer.requestCard(activePlayer.getHand().getCards(), RequestCause.EXCHANGE_DISCARD_CARD);
            CardStack<PlayableCard> discardStack = activePlayer.requestCardStack(getContext().getStackContainer().getBasicStacks(), List.of(), RequestCause.EXCHANGE_DISCARD_STACK);
            discardStack.addCardToBottom(card);
            activePlayer.removeCardFromHand(card);

            // Choose which stack to take from
            CardStack<PlayableCard> takeStack = activePlayer.requestCardStack(
                    getContext().getStackContainer().getBasicStacks(),
                    getContext().getStackContainer().getBasicStacks().stream().filter((s) -> s.getSize() == 0).map(CardStack::getUuid).toList(),
                    RequestCause.EXCHANGE_TAKE_STACK);

            boolean searchStack = activePlayer.requestBoolean(RequestCause.EXCHANGE_SEARCH);
            if(searchStack) {
                // todo
                // ask which resources to pay with
                // pay resources
                // ask which card in stack
                // add that card to player hand
                // shuffle stack
            } else {
                activePlayer.addCardToHand(takeStack.takeTopCard());
            }
        }
    }
}
