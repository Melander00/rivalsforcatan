package samuel.card;

import samuel.point.Point;
import samuel.point.PointBundle;

import java.util.Collection;

/**
 * For anything that can contain points.
 */
public interface PointHolder {

    /**
     * Returns the amount of points this object holds.
     * @return
     */
    PointBundle getPoints();
}
