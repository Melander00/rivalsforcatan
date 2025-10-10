package samuel.resource;

import samuel.card.CardID;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// todo: change so that we dont have the type as an instance, we reference it via the class, e.g OreResource.class. Also: implement IResourceBundle
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
}
