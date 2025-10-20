package samuel.effect;

/**
 * One time effect for a player.
 */
public interface Effect {

    /**
     * Whether the effect has been used.
     * @return
     */
    boolean hasBeenUsed();
}
