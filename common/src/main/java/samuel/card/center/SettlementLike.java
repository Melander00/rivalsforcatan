package samuel.card.center;

/**
 * Anything that is similar to Settlement and City
 */
public interface SettlementLike {

    /**
     * How many expansion slots above and below the settlement (not combined).
     * Example; Settlement: 1, City: 2
     * @return
     */
    int getExpansionSlots();

}
