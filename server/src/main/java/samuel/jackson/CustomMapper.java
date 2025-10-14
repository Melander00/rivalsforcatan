package samuel.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import samuel.jackson.mixin.PointClassMixin;
import samuel.jackson.mixin.ResourceClassMixin;
import samuel.point.PointAmount;
import samuel.resource.ResourceAmount;

public class CustomMapper extends ObjectMapper {

    private static CustomMapper mapper;

    public static CustomMapper getInstance() {
        if(mapper == null) {
            mapper = new CustomMapper();
            addMixins(mapper);
        }

        return mapper;
    }

    private static void addMixins(CustomMapper mapper) {

        mapper.addMixIn(ResourceAmount.class, ResourceClassMixin.class);
        mapper.addMixIn(PointAmount.class, PointClassMixin.class);

    }


}
