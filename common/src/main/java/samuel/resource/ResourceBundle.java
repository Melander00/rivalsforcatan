package samuel.resource;

import samuel.resource.resources.*;

import java.util.*;

public class ResourceBundle implements Iterable<ResourceAmount> {

    private final Map<Class<? extends Resource>, Integer> resources = new HashMap<>();

    public void addResource(Class<? extends Resource> resource, int amount) {
        Integer current = this.resources.get(resource);

        int total = current == null ? amount : current + amount;

        resources.put(resource, total);
    }

    public int getAmount(Class<? extends Resource> resource) {
        return resources.getOrDefault(resource, 0);
    }


    @Override
    public Iterator<ResourceAmount> iterator() {
        Iterator<Map.Entry<Class<? extends Resource>, Integer>> base = resources.entrySet().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() { return base.hasNext(); }

            @Override
            public ResourceAmount next() {
                Map.Entry<Class<? extends Resource>, Integer> entry = base.next();
                return new ResourceAmount(entry.getKey(), entry.getValue());
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceBundle that)) return false;

        for(Map.Entry<Class<? extends Resource>, Integer> res : resources.entrySet()) {
            if(that.getAmount(res.getKey()) != res.getValue()) {
                return false;
            }
        }

        return Objects.equals(resources, that.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(resources);
    }

    public static ResourceBundle oneOfAll() {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(LumberResource.class, 1);
        bundle.addResource(GoldResource.class, 1);
        bundle.addResource(GrainResource.class, 1);
        bundle.addResource(BrickResource.class, 1);
        bundle.addResource(WoolResource.class, 1);
        bundle.addResource(OreResource.class, 1);
        return bundle;
    }

    public static ResourceBundle oneOfAll(int amount) {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(LumberResource.class, amount);
        bundle.addResource(GoldResource.class, amount);
        bundle.addResource(GrainResource.class, amount);
        bundle.addResource(BrickResource.class, amount);
        bundle.addResource(WoolResource.class, amount);
        bundle.addResource(OreResource.class, amount);
        return bundle;
    }

    public static ResourceBundle fromAmount(ResourceAmount amount) {
        ResourceBundle bundle = new ResourceBundle();
        bundle.addResource(amount.resourceType(), amount.amount());
        return bundle;
    }

    public static ResourceBundle fromAmounts(List<ResourceAmount> amounts) {
        ResourceBundle bundle = new ResourceBundle();
        for(ResourceAmount amount : amounts) {
            bundle.addResource(amount.resourceType(), amount.amount());
        }
        return bundle;
    }
}
