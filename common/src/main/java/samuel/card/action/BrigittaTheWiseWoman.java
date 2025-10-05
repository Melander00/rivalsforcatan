package samuel.card.action;

import samuel.effect.RigProductionDieEffect;
import samuel.game.GameContext;
import samuel.player.Player;

public class BrigittaTheWiseWoman implements ActionCard {

    private static final String name = "Road";
    private static final String description = "Allows construction of a new settlement and of road complements.";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void play(Player owner, GameContext context) {
        owner.giveEffect(new RigProductionDieEffect(context));
    }
}
