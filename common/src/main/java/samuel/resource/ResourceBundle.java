package samuel.resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// todo: change so that we dont have the type as an instance, we reference it via the class, e.g OreResource.class. Also: implement IResourceBundle
public class ResourceBundle implements Iterable<ResourceAmount> {

    private final Map<Resource, Integer> resources = new HashMap<>();

    public void add(Resource resource, int amount) {
        resources.put(resource, amount);
    }

    public int getAmount(Resource resource) {
        return resources.getOrDefault(resource, 0);
    }


    @Override
    public Iterator<ResourceAmount> iterator() {
        Iterator<Map.Entry<Resource, Integer>> base = resources.entrySet().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() { return base.hasNext(); }

            @Override
            public ResourceAmount next() {
                Map.Entry<Resource, Integer> entry = base.next();
                return new ResourceAmount(entry.getKey(), entry.getValue());
            }
        };
    }
}
