package samuel.die.face;

import samuel.card.CardID;
import samuel.die.EventDieFace;
import samuel.event.die.CelebrationEvent;
import samuel.game.GameContext;
import samuel.player.Player;
import samuel.player.request.RequestCause;
import samuel.player.request.RequestCauseEnum;
import samuel.point.points.SkillPoint;
import samuel.resource.ResourceBundle;

import java.util.ArrayList;
import java.util.List;

public class CelebrationFace implements EventDieFace {

    private static final CardID id = new CardID("event_face", "celebration");

    @Override
    public void resolve(GameContext context) {

        context.getEventBus().fireEvent(new CelebrationEvent(context));

        List<Player> winners = new ArrayList<>();
        int mostPoints = 0;
        for(Player player : context.getPlayers()) {
            int points = player.getPoints(SkillPoint.class);
            if(points < mostPoints) continue;
            if(points > mostPoints) {
                mostPoints = points;
                winners.clear();
            }
            winners.add(player);
        }
        for(Player winner : winners) {
            giveResource(winner);
        }

    }

    private void giveResource(Player player) {
        ResourceBundle toGet = player.requestResource(ResourceBundle.oneOfAll(1), 1, new RequestCause(RequestCauseEnum.FREE_RESOURCES));
        player.giveResources(toGet);
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

