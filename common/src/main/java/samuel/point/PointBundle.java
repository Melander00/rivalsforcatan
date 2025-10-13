package samuel.point;

import samuel.resource.Resource;
import samuel.resource.ResourceAmount;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PointBundle implements Iterable<PointAmount> {

    private final Map<Class<? extends Point>, Integer> points = new HashMap<>();

    public void addPoint(Class<? extends Point> pointClass, int amount) {
        Integer currentPoints = this.points.get(pointClass);

        int totalPoints = currentPoints == null ? amount : currentPoints + amount;

        this.points.put(pointClass, totalPoints);
    }

    public int getAmount(Class<? extends Point> pointClass) {
        return this.points.getOrDefault(pointClass, 0);
    }

    @Override
    public Iterator<PointAmount> iterator() {
        Iterator<Map.Entry<Class<? extends Point>, Integer>> base = this.points.entrySet().iterator();

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return base.hasNext();
            }

            @Override
            public PointAmount next() {
                Map.Entry<Class<? extends Point>, Integer> entry = base.next();
                return new PointAmount(entry.getKey(), entry.getValue());
            }
        };
    }
}
