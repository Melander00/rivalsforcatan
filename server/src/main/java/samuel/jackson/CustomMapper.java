package samuel.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        mapper.addMixIn(ResourceAmount.class, ClassNameMixin.class);
        mapper.addMixIn(PointAmount.class, ClassNameMixin.class);

    }


}
