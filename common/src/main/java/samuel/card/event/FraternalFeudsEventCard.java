package samuel.card.event;

import samuel.card.CardID;
import samuel.card.PlayableCard;
import samuel.card.stack.CardStack;
import samuel.event.card.FraternalFeudsEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.player.PlayerHand;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;

import java.util.List;
import java.util.UUID;

public class FraternalFeudsEventCard implements EventCard {

    private static final CardID id = new CardID("event", "fraternal_feuds");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public CardID getCardID() {
        return id;
    }


    @Override
    public void resolveEvent(GameContext context) {
        context.getEventBus().fireEvent(new FraternalFeudsEvent());

        Player advantage = context.getStrengthAdvantage();
        if(advantage == null) return;

        for(Player player : context.getPlayers()) {
            if(player.equals(advantage)) continue;

            PlayerHand playerHand = player.getHand();
            for(int i = 0; i < 2; i++) {
                PlayableCard toRemove = advantage.requestCard(playerHand.getCards(), new RequestCause(RequestCauseEnum.FRATERNAL_FEUDS_REMOVE_CARD));
                CardStack<PlayableCard> stack = advantage.requestCardStack(context.getStackContainer().getBasicStacks(), List.of(), new RequestCause(RequestCauseEnum.STACK_TO_PLACE_CARD_IN));
                player.removeCardFromHand(toRemove);
                stack.addCardToBottom(toRemove);
            }
        }
    }
}
