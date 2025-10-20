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

    private final ActionHandler actionHandler = new DefaultActionHandler();
    private final DiceHandler diceHandler = new DefaultDiceHandler();
    private final ReplenishHandler replenishHandler = new DefaultReplenishHandler();
    private final ExchangeHandler exchangeHandler = new DefaultExchangeHandler();

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
        setupCardDeckAndStacks(deck, context);


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

    public ActionHandler getActionHandler() {
        return actionHandler;
    }

    public DiceHandler getDiceHandler() {
        return diceHandler;
    }

    public ReplenishHandler getReplenishHandler() {
        return replenishHandler;
    }

    public ExchangeHandler getExchangeHandler() {
        return exchangeHandler;
    }

    public abstract void setupPrincipality(Player player, int playerIndex);

    public void setupCardDeckAndStacks(Deck deck, GameContext context) {
        DefaultStackHandler.setupCardDeckAndStacks(deck, context, getBasicCardStacks(), getThemeCardStacks());
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
        getDiceHandler().rollAndResolveDice(activePlayer, context);

        getContext().setPhase(Phase.ACTION);
        getActionHandler().handleActionPhase(activePlayer, context);

        getContext().setPhase(Phase.REPLENISH);
        getReplenishHandler().handleReplenish(activePlayer, context);

        getContext().setPhase(Phase.EXCHANGE);
        getExchangeHandler().handleExchange(activePlayer, getContext());
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
                getActionHandler().handlePlayCardAction(activePlayer, context, res.first().getData(), res.second());

            } else {
                res.second().accept(false, ActionResponseType.INVALID_ACTION.toString());

            }
        }
    }


}
