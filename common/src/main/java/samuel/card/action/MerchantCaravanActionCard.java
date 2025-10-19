package samuel.card.action;

import samuel.card.CardID;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;

import java.util.UUID;

public class MerchantCaravanActionCard implements ActionCard {
    private static final CardID id = new CardID("action", "merchant_caravan");
    private final UUID uuid = UUID.randomUUID();

    @Override
    public CardID getCardID() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }


    @Override
    public boolean canPlay(Player player, GameContext context) {
        if(!context.getPhase().equals(Phase.ACTION)) return false;
        int amount = 0;
        for(ResourceAmount am : player.getResources()) {
            amount += am.amount();
        }
        return amount >= 2;
    }

    @Override
    public void onPlay(Player player, GameContext context) {

        ResourceBundle toRemove = player.requestResource(player.getResources(), 2, new RequestCause(RequestCauseEnum.TO_PAY_WITH));
        player.removeResources(toRemove);
        ResourceBundle toGet = player.requestResource(ResourceBundle.oneOfAll(2), 2, new RequestCause(RequestCauseEnum.FREE_RESOURCES));
        player.giveResources(toGet);
    }
}
