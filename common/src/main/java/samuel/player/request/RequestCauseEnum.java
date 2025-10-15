package samuel.player.request;

public enum RequestCauseEnum {
    INITIAL_DRAW,
    PLACE_CARD,
    REPLENISH,
    REPLENISH_STACK,
    EXCHANGE,
    EXCHANGE_DISCARD_CARD,
    EXCHANGE_DISCARD_STACK,
    EXCHANGE_TAKE_STACK,
    EXCHANGE_SEARCH,

    TRADE_PAY,
    TRADE_GET,

    CHOOSE_REGION_TO_INCREASE_RESOURCE,
    CHOOSE_REGION_TO_DECREASE_RESOURCE,

    FREE_RESOURCES,


    RIG_PRODUCTION_DIE,
    ;

    @Override
    public String toString() {
        return super.toString();
    }
}

