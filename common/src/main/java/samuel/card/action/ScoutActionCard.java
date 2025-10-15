package samuel.card.action;

import samuel.card.CardID;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;

import java.util.UUID;

public class ScoutActionCard implements ActionCard {

    private static final CardID id = new CardID("action", "scout");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public boolean canPlay(Player player, GameContext context) {
        return context.getPhase().equals(Phase.ACTION);
    }

    @Override
    public void onPlay(Player player, GameContext context) {
        // todo: implement
    }

    @Override
    public CardID getCardID() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }
}
