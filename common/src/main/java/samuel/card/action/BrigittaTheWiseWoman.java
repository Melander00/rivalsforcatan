package samuel.card.action;

import samuel.effect.RigProductionDieEffect;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.util.CardID;

public class BrigittaTheWiseWoman implements ActionCard {

    private static final CardID id = new CardID("basic", "brigitta_the_wise_woman");

    @Override
    public CardID getId() {
        return id;
    }

    @Override
    public void play(Player owner, GameContext context) {
        owner.giveEffect(new RigProductionDieEffect(context));
    }
}
