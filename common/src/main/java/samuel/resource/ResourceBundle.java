package samuel.resource;

import samuel.resource.resources.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
