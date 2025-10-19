package samuel.game;

import samuel.die.EventDieFace;
import samuel.event.die.EventDieEvent;
import samuel.event.die.ProductionDieEvent;
import samuel.player.Player;

public class DefaultDiceHandler implements DiceHandler {

    public void rollAndResolveDice(Player activePlayer, GameContext context) {
        int rollResults = rollProductionDice(activePlayer, context);

        EventDieFace eventResults = rollEventDice(activePlayer, context);

        if(eventResults.hasPriorityOverProduction()) {
            // Event first
            context.getEventBus().fireEvent(new EventDieEvent.Post(eventResults));
            eventResults.resolve(context);
            // Then production
            context.getEventBus().fireEvent(new ProductionDieEvent.Post(rollResults, context));
        } else {
            // Production first
            context.getEventBus().fireEvent(new ProductionDieEvent.Post(rollResults, context));
            // Then event
            context.getEventBus().fireEvent(new EventDieEvent.Post(eventResults));
            eventResults.resolve(context);
        }
    }

    public int rollProductionDice(Player activePlayer, GameContext context) {
        int res = context.rollProductionDie();
        ProductionDieEvent productionEvent = new ProductionDieEvent(activePlayer, res);
        context.getEventBus().fireEvent(productionEvent);
        return productionEvent.getRollResults();
    }

    public EventDieFace rollEventDice(Player activePlayer, GameContext context) {
        EventDieFace face = context.rollEventDice();
        EventDieEvent eventEvent = new EventDieEvent(activePlayer, face);
        context.getEventBus().fireEvent(eventEvent);
        return eventEvent.getRollResults();
    }
}
