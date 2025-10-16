package samuel.die.face;

import samuel.board.BoardPosition;
import samuel.card.CardID;
import samuel.card.ResourceHolder;
import samuel.die.EventDieFace;
import samuel.event.die.BrigandAttackEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.resource.Resource;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.GoldResource;
import samuel.resource.resources.WoolResource;

import java.util.*;

public class BrigandAttackFace implements EventDieFace {

    private static final CardID id = new CardID("event_face", "brigand_attack");

    private static final int minResources = 7;

    private static final List<Class<? extends Resource>> toRemoveFrom = List.of(
            GoldResource.class,
            WoolResource.class
    );

    @Override
    public void resolve(GameContext context) {
        //  If you have more than 7 resources, you lose all your gold and wool supplies.

        for(Player player : context.getPlayers()) {

            Map<ResourceHolder, Integer> map = getAllHolders(player);


            BrigandAttackEvent event = new BrigandAttackEvent(player, map);
            context.getEventBus().fireEvent(event);
            Map<ResourceHolder, Integer> finalMap = event.getResourceHolders();

            int amountOfResources = 0;
            for(Integer res : finalMap.values()) {
                amountOfResources += res;
            }

            /*
            todo:
                How do we handle Gold Cache and similar?
                It is supposed to not lose the resources on brigand attack.
                We cant simply only remove those from the finalMap since
                > If, despite, this more than 7 resources are counted in your principality,
                > you may also lose gold and/or wool in a region adjacent to a Storehouse
                -
                -
                Option 1: SafeFromBrigandAttack interface
                    pros: easy to reuse, implement and extend
                    cons: Creating an interface for a non-general purpose,
                          increases bloat in project if we stick to this pattern.
                -
                Option 2: Emit an event for each holder that is cancelable
                    pros: Only those needed have to listen to the event
                    cons: We flood the event bus with a lot of unnecessary events (for normal regions)
                -
                Option 3: In the map, we don't remove if they shouldn't be targeted,
                          we simply set their amount to zero and if they shouldn't be reset we remove them.
                          Basically: Storehouse -> set amount to zero; Gold Cache -> remove itself
                    pros: no need for bloat
                    cons: no con? since we still need the map to determine resources.
                -
                We go for Option 3
             */

            if(amountOfResources > minResources) {
                ResourceBundle bundle = resetResources(finalMap);
                context.getEventBus().fireEvent(new BrigandAttackEvent.Post(player, bundle));
            }
        }
    }

    private Map<ResourceHolder, Integer> getAllHolders(Player player) {
        Map<ResourceHolder, Integer> map = new HashMap<>();

        // we map all the resource holders to how much they contain.
        // we force the cards that handle Brigand Event to manage whether they should
        // stay in this map or not.
        // For example:
        //  Gold Cache should listen to the event and remove itself from this map
        //  Storehouse should set its neighbours to zero from this.
        for(BoardPosition pos : player.getPrincipality()) {
            if(pos.isEmpty()) continue;
            if (pos.getCard() instanceof ResourceHolder holder) {
                int amount = 0;
                for (ResourceAmount am : holder.getResources()) {
                    amount += am.amount();
                }
                if(amount > 0) map.put(holder, amount);
            }
        }
        return map;
    }

    private ResourceBundle resetResources(Map<ResourceHolder, Integer> map) {
        // Set all gold and wool to zero
        ResourceBundle bundle = new ResourceBundle();
        for(ResourceHolder holder : map.keySet()) {
            Class<? extends Resource> type = holder.getType();
            if(toRemoveFrom.contains(type)) {
                ResourceBundle has = holder.getResources();
                for(ResourceAmount am : has) {
                    bundle.addResource(am.resourceType(), am.amount());
                }
                holder.decreaseAmount(Integer.MAX_VALUE); // instead of hard-coding 3 or adding a reset method lmao.
            }
        }
        return bundle;
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
