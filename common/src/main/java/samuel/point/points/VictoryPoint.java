package samuel.point.points;

import samuel.point.Point;

import java.util.ArrayList;
import java.util.Collection;

public class VictoryPoint implements Point {

    @Override
    public String getName() {
        return "Victory Point";
    }

    public static Collection<VictoryPoint> create(int amount) {
        Collection<VictoryPoint> points = new ArrayList<>();
        for(int i = 0; i < amount; i++) {
            points.add(new VictoryPoint());
        }
        return points;
    }
}
