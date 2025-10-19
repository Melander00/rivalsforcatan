package samuel.card.event;

import samuel.board.Board;
import samuel.board.BoardPosition;
import samuel.card.CardID;
import samuel.card.ExpansionCard;
import samuel.card.building.AbbeyBuildingCard;
import samuel.card.building.StorehouseBuildingCard;
import samuel.card.region.RegionCard;
import samuel.card.util.ExpansionCardHelper;
import samuel.event.card.YearOfPlentyEvent;
import samuel.game.GameContext;
import samuel.player.Player;

import java.util.List;
import java.util.UUID;

public class YearOfPlentyEventCard implements EventCard {

    private static final CardID id = new CardID("event", "year_of_plenty");

    private final UUID uuid = UUID.randomUUID();

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
        context.getEventBus().fireEvent(new YearOfPlentyEvent());

        for(Player player : context.getPlayers()) {
            Board board = player.getPrincipality();
            for(BoardPosition pos : board) {
                if(pos.isEmpty()) continue;

                if(pos.getCard() instanceof StorehouseBuildingCard || pos.getCard() instanceof AbbeyBuildingCard) {
                    List<RegionCard> regions = ExpansionCardHelper.getNeighbouringRegions(pos);
                    for(RegionCard region : regions) {
                        region.increaseResource(1);
                    }
                }
            }
        }
    }
}