package samuel.resource;

import samuel.point.Point;

public interface IResourceAmount {
    Class<? extends Resource> getResourceType();
    int getAmount();
}
