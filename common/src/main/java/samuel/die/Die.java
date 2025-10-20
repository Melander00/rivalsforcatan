package samuel.die;

import java.util.List;

public interface Die<Face> {

    /**
     * Sets the faces to be rolled by this die.
     * @param faces
     */
    default void setFaces(List<Face> faces) {}

    /**
     * Rolls the die
     * @return
     */
    Face rollDie();
}
