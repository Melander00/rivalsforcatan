package samuel.event;

public record EventID(String namespace, String id) {
    @Override
    public String toString() {
        return this.namespace + ":" + this.id;
    }

    public boolean equals(EventID obj) {
        return obj.namespace.equals(this.namespace) && obj.id.equals(this.id);
    }
}
