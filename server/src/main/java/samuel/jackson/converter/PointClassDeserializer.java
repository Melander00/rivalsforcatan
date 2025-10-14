package samuel.jackson.converter;

import samuel.jackson.ClassNameConverter;

public class PointClassDeserializer extends ClassNameConverter.Deserializer{
    @Override
    public String getBasePackage() {
        return "samuel.point.points";
    }

    @Override
    public ClassNameConverter.Deserializer create(String pkg) {
        return new PointClassDeserializer();
    }

}
