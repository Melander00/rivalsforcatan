package samuel.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public abstract class ClassNameMixin {
    @JsonSerialize(using = ClassNameConverter.Serializer.class)
    @JsonDeserialize(using = ClassNameConverter.Deserializer.class)
    abstract Class<?> resourceType();

    @JsonSerialize(using = ClassNameConverter.Serializer.class)
    @JsonDeserialize(using = ClassNameConverter.Deserializer.class)
    abstract Class<?> pointType();
}
