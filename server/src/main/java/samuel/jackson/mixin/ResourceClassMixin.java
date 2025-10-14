package samuel.jackson.mixin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import samuel.jackson.ClassNameConverter;
import samuel.jackson.converter.ResourceClassDeserializer;

public abstract class ResourceClassMixin {
    @JsonSerialize(using = ClassNameConverter.Serializer.class)
    @JsonDeserialize(using = ResourceClassDeserializer.class)
    abstract Class<?> resourceType();
}
