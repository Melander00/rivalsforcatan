package samuel.event.player;

import samuel.card.region.RegionCard;
import samuel.die.EventDieFace;
import samuel.event.CancelableEvent;
import samuel.event.ContextEvent;
import samuel.event.EventID;
import samuel.event.PlayerEvent;
import samuel.game.GameContext;
import samuel.player.Player;

import java.util.List;

public class PlayerTakeRegionCardsEvent implements PlayerEvent, ContextEvent {

    private static final EventID id = new EventID("player", "take_regions");

    private final Player player;
    private List<RegionCard> regions;
    private GameContext context;

    public PlayerTakeRegionCardsEvent(Player player, List<RegionCard> regions, GameContext context) {
        this.player = player;
        this.regions = regions;
        this.context = context;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public EventID getId() {
        return id;
    }

    public List<RegionCard> getRegions() {
        return regions;
    }

    public void setRegions(List<RegionCard> regions) {
        this.regions = regions;
    }

    @Override
    public GameContext getContext() {
        return context;
    }
}
