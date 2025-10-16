package samuel.die.face;

import samuel.card.CardID;
import samuel.card.ResourceHolder;
import samuel.die.EventDieFace;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;

public class BrigandAttackFace implements EventDieFace {

    private static final CardID id = new CardID("event_face", "brigand_attack");

    private static final int minResources = 7;

    @Override
    public void resolve(GameContext context) {
        //  If you have more than 7 resources, you lose all your gold and wool supplies.

        for(Player player : context.getPlayers()) {
            ResourceBundle bundle = player.getResources();
            int amountOfResources = 0;
            for(ResourceAmount am : bundle) {
                amountOfResources += am.amount();
            }



            if(amountOfResources > minResources) {
                // Set all gold and wool to zero
                player.getPrincipality().forEach(boardPosition -> {
                    if(boardPosition.isEmpty()) return;

                    if(boardPosition.getCard() instanceof ResourceHolder holder) {
                        holder.decreaseAmount(3);
                    }
                });
            }
        }
    }

    @Override
    public boolean hasPriorityOverProduction() {
        return true;
    }

    @Override
    public CardID getId() {
        return id;
    }
}
