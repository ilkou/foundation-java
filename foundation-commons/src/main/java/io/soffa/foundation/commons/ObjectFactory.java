package io.soffa.foundation.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;


public final class ObjectFactory {

    private ObjectFactory() {
    }

    private static class IgnoreAnnotations extends JacksonAnnotationIntrospector {

        private static final long serialVersionUID = 1L;

        @Override
        public JsonProperty.Access findPropertyAccess(Annotated m) {
            return JsonProperty.Access.AUTO;
        }

        @Override
        public boolean hasIgnoreMarker(AnnotatedMember m) {
            return false;
        }
    }

    public static ObjectMapper create() {
        return create(false);
    }

    public static ObjectMapper create(boolean ignoreAccessAnnotations) {
        JsonMapper.Builder builder = JsonMapper.builder()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
        return create(builder.build(), ignoreAccessAnnotations);
    }

    public static ObjectMapper create(ObjectMapper mapper, boolean ignoreAccessAnnotations) {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Date.class, new DateDeserializers.DateDeserializer() {
            @Override
            public Date deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                try {
                    return super.deserialize(parser, ctxt);
                } catch (InvalidFormatException e) {
                    if (parser.hasToken(JsonToken.VALUE_STRING)) {
                        String string = parser.getText().trim();
                        return DateUtil.parse(string);
                    }
                    throw e;
                }
            }
        });

        if (ignoreAccessAnnotations) {
            mapper.setAnnotationIntrospector(new IgnoreAnnotations());
        }

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
            .registerModule(simpleModule);
    }

    @SneakyThrows
    public static <T> T deserialize(ObjectMapper mapper, String jsonString, Class<T> type) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        if (type == String.class) {
            return type.cast(jsonString);
        }
        return mapper.readValue(jsonString, type);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public static <T> T convert(ObjectMapper mapper, Object input, Class<T> type) {
        if (input == null) {
            return ClassUtil.newInstance(type);
        }
        if (type.isInstance(input)) {
            return type.cast(input);
        }
        if (type == String.class) {
            return (T) mapper.writeValueAsString(input);
        }
        return mapper.convertValue(input, type);
    }

    @SneakyThrows
    public static String serialize(ObjectMapper mapper, Object src) {
        if (src == null) return null;
        if (src instanceof String) return src.toString();
        return mapper.writeValueAsString(src);
    }

    @SneakyThrows
    public static void serializeToFile(ObjectMapper mapper, Object content, File file) {
        if (content != null) {
            FileUtils.writeStringToFile(file, serialize(mapper, content), StandardCharsets.UTF_8);
        }
    }

    @SneakyThrows
    public static <T> Map<String, T> deserializeMap(ObjectMapper mapper, String input) {
        if (input == null) {
            new HashMap<>();
        }
        MapLikeType mapType = mapper.getTypeFactory().constructMapLikeType(Map.class, String.class, Object.class);
        return mapper.readValue(input, mapType);
    }

    @SneakyThrows
    public static <T> Map<String, T> deserializeMap(ObjectMapper mapper, InputStream input) {
        if (input == null) {
            new HashMap<>();
        }
        MapLikeType mapType = mapper.getTypeFactory().constructMapLikeType(Map.class, String.class, Object.class);
        return mapper.readValue(input, mapType);
    }

    @SneakyThrows
    public static <T> Map<String, T> deserializeMap(ObjectMapper mapper, InputStream input, Class<T> type) {
        if (input == null) {
            return new HashMap<>();
        }
        MapLikeType mapType = mapper.getTypeFactory().constructMapLikeType(Map.class, String.class, type);
        return mapper.readValue(input, mapType);
    }

    @SneakyThrows
    public static <T> Map<String, T> deserializeMap(ObjectMapper mapper, String input, Class<T> type) {
        if (input == null) {
            return new HashMap<>();
        }
        MapLikeType mapType = mapper.getTypeFactory().constructMapLikeType(Map.class, String.class, type);
        return mapper.readValue(input, mapType);
    }

    @SneakyThrows
    public static <T> T deserializeParametricType(ObjectMapper mapper, String input, Class<?> rawType, Class<?>... parameterClasses) {
        if (input == null) {
            return null;
        }
        JavaType type = mapper.getTypeFactory().constructParametricType(rawType, parameterClasses);
        return mapper.readValue(input, type);
    }

    @SneakyThrows
    public static <T> List<T> deserializeList(ObjectMapper mapper, String input, Class<T> type) {
        if (StringUtils.isBlank(input)) {
            return new ArrayList<>();
        }
        ArrayType mapType = mapper.getTypeFactory().constructArrayType(type);
        return Arrays.asList(mapper.readValue(input, mapType));
    }

    @SneakyThrows
    public static <T> List<T> deserializeList(ObjectMapper mapper, InputStream input, Class<T> type) {
        if (input == null) {
            return new ArrayList<>();
        }
        ArrayType mapType = mapper.getTypeFactory().constructArrayType(type);
        return Arrays.asList(mapper.readValue(input, mapType));
    }


    @SuppressWarnings("unchecked")
    static <E> Map<String, E> toMap(ObjectMapper mapper, Object input, Class<E> valueClass) {
        if (input == null) {
            return new HashMap<>();
        }
        if (input instanceof Map) {
            return (Map<String, E>) input;
        }
        MapLikeType type = mapper.getTypeFactory().constructMapLikeType(Map.class, String.class, valueClass);
        if (input instanceof String) {
            try {
                return mapper.readValue((String) input, type);
            } catch (IOException e) {
                return new HashMap<>();
            }
        } else {
            return mapper.convertValue(input, type);
        }
    }

}
