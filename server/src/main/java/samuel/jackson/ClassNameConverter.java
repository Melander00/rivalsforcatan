package samuel.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

public class ClassNameConverter {


    // Serializer (simple, just prints simple name)
    public static class Serializer extends JsonSerializer<Class<?>> implements ContextualSerializer {
        @Override
        public void serialize(Class<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getSimpleName());
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
            return this; // no special field-specific logic needed
        }
    }

    // Deserializer that can use field name or type to infer package dynamically
    public static class Deserializer extends JsonDeserializer<Class<?>> implements ContextualDeserializer {
        private String basePackage;

        public Deserializer() {
            this.basePackage = null; // default, will be set in createContextual
        }

        private Deserializer(String basePackage) {
            this.basePackage = basePackage;
        }

        public String getBasePackage() {
            return basePackage;
        }

        @Override
        public Class<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String simpleName = p.getValueAsString();
            String pkg = getBasePackage() != null ? getBasePackage() : "samuel.resource.resources"; // fallback
//            System.out.println(pkg + " | " + getBasePackage());
            try {
                return Class.forName(pkg + "." + simpleName);
            } catch (ClassNotFoundException e) {
                throw new IOException("Unknown class: " + simpleName, e);
            }
        }

        public Deserializer create(String pkg) {
            return new Deserializer(pkg);
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
            if (property != null) {
//                // Example: dynamically decide package based on field name or type
//                String pkg = property.getType().getRawClass().getPackageName();
                // Use the package of the class that *owns* this property
                Class<?> owner = property.getMember() != null
                        ? property.getMember().getDeclaringClass()
                        : null;

                String pkg = (owner != null)
                        ? owner.getPackageName()
                        : "samuel.resource.resources"; // default/fallback
                return create(pkg);
            }
            return this;
        }
    }

}
