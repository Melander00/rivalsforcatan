package samuel.introductory;

import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.game.AbstractGame;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;

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
    public UUID setupInitialDraw(Player player, List<CardStack<PlayableCard>> cardStacks, List<UUID> usedCardStackIds) {
        CardStack<PlayableCard> stack = player.requestCardStack(cardStacks, usedCardStackIds, new RequestCause(RequestCauseEnum.INITIAL_DRAW));
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
    public void runTurn(Player activePlayer) {
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
