package samuel.effect;

import samuel.board.BoardPosition;
import samuel.card.region.RegionCard;
import samuel.card.stack.CardStack;
import samuel.event.player.PlayerTakeRegionCardsEvent;
import samuel.eventmanager.Subscribe;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;

import java.util.ArrayList;
import java.util.List;

public class ChooseRegionCardsEffect implements Effect {

    private boolean hasBeenUsed = false;
    private final Player player;

    public ChooseRegionCardsEffect(Player owner, GameContext context) {
        player = owner;
        context.getEventBus().register(this);
    }


    @Subscribe
    public void onPlayerChooseRegionEvent(PlayerTakeRegionCardsEvent event) {
        if(!event.getPlayer().equals(player)) return;

        List<RegionCard> cards = new ArrayList<>();

        CardStack<RegionCard> cardStack = event.getContext().getStackContainer().getRegionStack();
        int leftToTake = Math.min(2, cardStack.getSize());
        while(leftToTake > 0) {

            RegionCard card = player.requestCard(cardStack.getCards(), new RequestCause(RequestCauseEnum.PICK_CARD));
            RegionCard toGet = cardStack.takeCardByUuid(card.getUuid());
            cards.add(toGet);

            leftToTake--;

        }

        event.setRegions(cards);
        this.hasBeenUsed = true;

        player.removeEffect(this);
    }

    @Override
    public boolean hasBeenUsed() {
        return false;
    }


}
