package samuel.die;

import java.util.List;

public interface Die<Face> {

    default void setFaces(List<Face> faces) {}

    Face rollDie();

}
