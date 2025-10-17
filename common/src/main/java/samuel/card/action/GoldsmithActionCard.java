package samuel.card.action;

import samuel.card.CardID;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.GoldResource;

import java.util.UUID;

public class GoldsmithActionCard implements ActionCard {
    private static final CardID id = new CardID("action", "goldsmith");
    private final UUID uuid = UUID.randomUUID();

    @Override
    public CardID getCardID() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean canPlay(Player player, GameContext context) {
        if(!context.getPhase().equals(Phase.ACTION)) return false;

        return player.getResources(GoldResource.class) >= 3;
    }

    @Override
    public void onPlay(Player player, GameContext context) {
        ResourceBundle goldCost = new ResourceBundle();
        goldCost.addResource(GoldResource.class, 3);
        player.removeResources(goldCost);
        ResourceBundle toGet = player.requestResource(ResourceBundle.oneOfAll(2), 2, new RequestCause(RequestCauseEnum.FREE_RESOURCES));
        player.giveResources(toGet);
    }
}
