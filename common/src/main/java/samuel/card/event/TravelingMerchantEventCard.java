package samuel.card.event;

import samuel.card.CardID;
import samuel.event.card.TravelingMerchantEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.resource.ResourceAmount;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.GoldResource;

import java.util.UUID;

public class TravelingMerchantEventCard implements EventCard {

    private static final CardID id = new CardID("event", "traveling_merchant");

    private final UUID uuid = UUID.randomUUID();

    private static final int maxResources = 2;

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public CardID getCardID() {
        return id;
    }


    @Override
    public void resolveEvent(GameContext context) {
        context.getEventBus().fireEvent(new TravelingMerchantEvent());

        for(Player player : context.getPlayers()) {
            int gold = player.getResources(GoldResource.class);

            if(gold == 0) continue;

            int max = Math.min(gold, maxResources);

            int wants = player.requestInt(0, max, new RequestCause(RequestCauseEnum.AMOUNT_TO_TRADE));

            player.removeResources(ResourceBundle.fromAmount(new ResourceAmount(GoldResource.class, wants)));

            ResourceBundle bundle = player.requestResource(ResourceBundle.oneOfAll(wants), wants, new RequestCause(RequestCauseEnum.FREE_RESOURCES));
            player.giveResources(bundle);
        }
    }
}
