package samuel.action;

public enum ActionResponseType {
    BAD_PHASE,

    CARD_NOT_FOUND,
    CARD_CANNOT_BE_PLAYED,

    INVALID_ACTION,
    ;

    @Override
    public String toString() {
        return super.toString();
    }
}
