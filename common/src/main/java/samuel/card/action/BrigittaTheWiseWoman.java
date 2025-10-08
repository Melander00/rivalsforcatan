package samuel.card.action;

import samuel.effect.RigProductionDieEffect;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.util.CardID;

import java.util.UUID;

public class BrigittaTheWiseWoman implements ActionCard {

    private static final CardID id = new CardID("action", "brigitta_the_wise_woman");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public CardID getCardID() {
        return id;
    }

//    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void play(Player owner, GameContext context) {
        owner.giveEffect(new RigProductionDieEffect(owner, context));
    }
}
