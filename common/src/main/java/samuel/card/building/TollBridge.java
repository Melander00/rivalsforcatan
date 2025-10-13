package samuel.card.building;

import samuel.board.BoardPosition;
import samuel.card.*;
import samuel.card.util.ExpansionCardHelper;
import samuel.point.PointBundle;
import samuel.point.points.CommercePoint;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.BrickResource;
import samuel.resource.resources.TimberResource;

import java.util.UUID;

public class TollBridge implements PlaceableCard, PriceTag, PointHolder {

    private static final CardID cardId = new CardID("building", "toll_bridge");

    private final UUID uuid = UUID.randomUUID();

    @Override
    public CardID getCardID() {
        return cardId;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public PointBundle getPoints() {
        PointBundle bundle = new PointBundle();
        bundle.addPoint(CommercePoint.class, 1);
        return bundle;
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(TimberResource.class, 1);
        bundle.addResource(BrickResource.class, 1);
        return bundle;
    }

    @Override
    public boolean validatePlacement(BoardPosition position) {
        if(!position.isEmpty()) return false;
        return ExpansionCardHelper.validatePlacement(position);
    }
}
