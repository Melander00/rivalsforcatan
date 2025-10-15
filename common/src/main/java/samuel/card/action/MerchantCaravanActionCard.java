package samuel.card.action;

import samuel.card.CardID;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;

import java.util.UUID;

public class MerchantCaravanActionCard implements ActionCard {
    private static final CardID id = new CardID("action", "brigitta_the_wise_woman");
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
        return context.getPhase().equals(Phase.ACTION);
    }

    @Override
    public void onPlay(Player player, GameContext context) {
        // todo: implement
    }
}
