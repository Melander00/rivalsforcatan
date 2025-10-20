package samuel.die.face;

import samuel.card.CardID;
import samuel.die.EventDieFace;
import samuel.event.die.TradeEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;

public class TradeFace implements EventDieFace {

    private static final CardID id = new CardID("event_face", "trade");

    @Override
    public void resolve(GameContext context) {

        context.getEventBus().fireEvent(new TradeEvent(context));

        Player advantage = context.getTradeAdvantage();
        if(advantage == null) return;

        for(Player player : context.getPlayers()) {

            if(player.equals(advantage)) continue;

            ResourceBundle resources = player.getResources();

            int amount = 0;
            for(ResourceAmount am : resources) {
                amount += am.amount();
            }
            if(amount == 0) continue;

            ResourceBundle toGet = advantage.requestResource(resources, 1, new RequestCause(RequestCauseEnum.FREE_RESOURCES));

            player.removeResources(toGet);
            advantage.giveResources(toGet);
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
