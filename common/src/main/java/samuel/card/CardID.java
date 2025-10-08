package samuel.card;

/**
 * The reason for using CardID instead of just a String as id is so that we can keep namespaces in case
 * a card has a different implementation in a different expansion. For example if a certain Hero has a different
 * amount of skill points we can have both cards with same id, just different namespaces.
 * @param namespace
 * @param id
 */
public record CardID(String namespace, String id) {

    @Override
    public String toString() {
        return this.namespace + ":" + this.id;
    }

    public boolean equals(CardID obj) {
        return obj.namespace.equals(this.namespace) && obj.id.equals(this.id);
    }
}
