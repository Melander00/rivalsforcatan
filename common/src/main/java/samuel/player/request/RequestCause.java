package samuel.player.request;

public enum RequestCause {
    INITIAL_DRAW,
    PLACE_CARD,
    REPLENISH,
    REPLENISH_STACK,
    EXCHANGE,
    EXCHANGE_DISCARD_CARD,
    EXCHANGE_DISCARD_STACK,
    EXCHANGE_TAKE_STACK,
    EXCHANGE_SEARCH,


    RIG_PRODUCTION_DIE,
    ;

    @Override
    public String toString() {
        return super.toString();
    }
}
