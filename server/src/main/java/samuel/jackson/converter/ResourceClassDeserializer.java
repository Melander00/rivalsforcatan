package samuel.jackson.converter;

import samuel.jackson.ClassNameConverter;

public class ResourceClassDeserializer extends ClassNameConverter.Deserializer {

    @Override
    public String getBasePackage() {
        return "samuel.resource.resources";
    }

    @Override
    public ClassNameConverter.Deserializer create(String pkg) {
        return new ResourceClassDeserializer();
    }
}
