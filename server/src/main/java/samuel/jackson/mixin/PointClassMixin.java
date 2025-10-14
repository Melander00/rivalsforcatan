package samuel.jackson.mixin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import samuel.jackson.ClassNameConverter;
import samuel.jackson.converter.PointClassDeserializer;

public abstract class PointClassMixin {
    @JsonSerialize(using = ClassNameConverter.Serializer.class)
    @JsonDeserialize(using = PointClassDeserializer.class)
    abstract Class<?> pointType();
}
