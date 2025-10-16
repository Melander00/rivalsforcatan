package samuel.die;

import samuel.die.face.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventDie implements Die<EventDieFace> {

    private final Random random = new Random();
    private List<EventDieFace> faces = List.of(
            new BrigandAttackFace()
//            new TradeFace(),
//            new CelebrationFace(),
//            new PlentifulHarvestFace(),
//            new EventCardFace(),
//            new EventCardFace()
    );

    @Override
    public EventDieFace rollDie() {
        if(faces.isEmpty()) return null;
        int index = random.nextInt(faces.size());
        return faces.get(index);
    }

    @Override
    public void setFaces(List<EventDieFace> faces) {
        this.faces = faces;
    }
}
