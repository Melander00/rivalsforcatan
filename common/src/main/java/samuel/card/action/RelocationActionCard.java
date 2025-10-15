package samuel.card.action;

import samuel.card.CardID;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;

import java.util.UUID;

public class RelocationActionCard implements ActionCard {

    private static final CardID id = new CardID("action", "relocation");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public boolean canPlay(Player player, GameContext context) {
        return context.getPhase().equals(Phase.ACTION);
    }

    @Override
    public CardID getCardID() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void onPlay(Player player, GameContext context) {
        // todo: implement
    }
}
