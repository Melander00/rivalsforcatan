package samuel.resource;

// todo: change so that we dont have the type as an instance, we reference it via the class, e.g OreResource.class. Also: implement IResourceAmount
public record ResourceAmount(Resource resource, int amount) {
}
