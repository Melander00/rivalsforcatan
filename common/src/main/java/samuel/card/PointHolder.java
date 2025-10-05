package samuel.card;

import samuel.point.Point;

import java.util.Collection;

public interface PointHolder {
    Collection<? extends Point> getPoints();
}
