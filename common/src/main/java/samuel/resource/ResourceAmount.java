package samuel.resource;

public record ResourceAmount(Class<? extends Resource> resourceType, int amount) {
}
