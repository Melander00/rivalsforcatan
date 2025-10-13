package samuel.die.face;

import samuel.card.CardID;
import samuel.die.EventDieFace;
import samuel.event.die.PlentifulHarvestEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;

public class PlentifulHarvestFace implements EventDieFace {

    private static final CardID id = new CardID("event_face", "plentiful_harvest");

    @Override
    public void resolve(GameContext context) {
        context.getEventBus().fireEvent(new PlentifulHarvestEvent());
        for(Player player : context.getPlayers()) {
            /*
                todo:
                 Improvement: We use threads so both players can answer at the same time
                 Pros: Second player doesn't have to wait for the first player to choose
                 Cons: We are enforcing a concurrency pattern
            */



            ResourceBundle amount = player.requestResource(ResourceBundle.oneOfAll(), 1, RequestCause.FREE_RESOURCES);
            player.giveResources(amount);
        }
    }

    @Override
    public boolean hasPriorityOverProduction() {
        return false;
    }

    @Override
    public CardID getId() {
        return id;
    }
}
