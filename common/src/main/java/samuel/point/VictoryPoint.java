package samuel.point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VictoryPoint implements Point {

    public static Collection<VictoryPoint> create(int amount) {
        Collection<VictoryPoint> points = new ArrayList<>();
        for(int i = 0; i < amount; i++) {
            points.add(new VictoryPoint());
        }
        return points;
    }
}
