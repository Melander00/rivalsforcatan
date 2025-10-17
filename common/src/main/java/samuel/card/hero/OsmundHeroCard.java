package samuel.card.hero;

import samuel.card.CardID;
import samuel.game.GameContext;
import samuel.phase.Phase;
import samuel.player.Player;
import samuel.point.PointBundle;
import samuel.point.points.SkillPoint;
import samuel.point.points.StrengthPoint;
import samuel.resource.ResourceBundle;
import samuel.resource.resources.GrainResource;
import samuel.resource.resources.OreResource;
import samuel.resource.resources.WoolResource;

import java.util.UUID;

public class OsmundHeroCard implements HeroCard {
    private static final CardID id = new CardID("hero", "osmund");
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
        return context.getPhase().equals(Phase.ACTION) && player.hasResources(getCost());
    }

    @Override
    public PointBundle getPoints() {
        PointBundle bundle = new PointBundle();
        bundle.addPoint(StrengthPoint.class, 2);
        bundle.addPoint(SkillPoint.class, 2);
        return bundle;
    }

    @Override
    public ResourceBundle getCost() {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(GrainResource.class, 1);
        bundle.addResource(WoolResource.class, 1);
        bundle.addResource(OreResource.class, 1);
        return bundle;
    }
}
