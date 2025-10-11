package samuel.game;

import samuel.card.Card;
import samuel.card.stack.CardStack;
import samuel.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        // setup principality
        for(int i = 0; i < context.getPlayers().size(); i++ ) {
            setupPrincipality(context.getPlayers().get(i), i);
        }

        // setup card deck and stacks
        setupCardDeckAndStacks();

        // initial draw
        List<CardStack<Card>> cardStacks = context.getStackContainer().getBasicStacks();
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

    abstract UUID setupInitialDraw(Player player, List<CardStack<Card>> cardStacks, List<UUID> usedCardStackIds);

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
}
