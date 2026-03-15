package org.opentmf.commons.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import lombok.Generated;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.type.TypeReference;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

/**
 * @author Gökhan Demir
 */
public final class JacksonUtil {

  private static final SimpleModule PERMISSIVE_DATETIME_MODULE = new SimpleModule();
  private static final DefaultPrettyPrinter PRETTY_PRINTER;
  private static volatile ObjectMapper OBJECT_MAPPER;

  static {
    PERMISSIVE_DATETIME_MODULE.addDeserializer(OffsetDateTime.class,
        new PermissiveDateTimeDeserializer());
    OBJECT_MAPPER = defaultMapperBuilder().build();

    var indenter = new DefaultIndenter("  ", "\n");
    PRETTY_PRINTER = new DefaultPrettyPrinter()
        .withObjectIndenter(indenter)
        .withArrayIndenter(indenter);
  }

  @Generated
  private JacksonUtil() {
    throw new UnsupportedOperationException(
        "JacksonUtil is a utility library with static methods, " + "hence cannot be instantiated.");
  }

  /**
   * Returns the default initialized objectMapper, which is capable of serializing and deserializing
   * OffsetDateTime in a safe way, disables writing dates as long values, and does not fail on
   * unknown properties.
   *
   * <p><strong>Important:</strong> Do not mutate the returned instance directly. Instead, use
   * {@link #defaultMapperBuilder()} to create a pre-configured builder, customize it (e.g. add
   * mix-ins, subtypes, modules), build a new mapper, and then call
   * {@link #setDefaultObjectMapper(ObjectMapper)} so that the utility methods in this class use
   * the customized instance.
   *
   * @return the default object mapper.
   */
  public static ObjectMapper getDefaultObjectMapper() {
    return OBJECT_MAPPER;
  }

  /**
   * Returns a {@link JsonMapper.Builder} pre-configured with the opentmf defaults:
   * <ul>
   *   <li>{@link JsonInclude.Include#NON_NULL} serialization inclusion</li>
   *   <li>Permissive OffsetDateTime deserializer (accepts ISO strings, epoch millis, etc.)</li>
   * </ul>
   *
   * <p>Jackson 3 defaults already disable {@code WRITE_DATES_AS_TIMESTAMPS},
   * {@code FAIL_ON_EMPTY_BEANS}, and {@code FAIL_ON_UNKNOWN_PROPERTIES}.
   *
   * <p>Callers can further customize the builder (e.g. {@code addMixIn}, {@code addModule},
   * {@code registerSubtypes}) before calling {@code build()}.
   *
   * <p>Example usage in a Spring Boot microservice:
   * <pre>{@code
   * @Configuration
   * public class JacksonConfig {
   *
   *   @Primary
   *   @Bean
   *   public ObjectMapper objectMapper() {
   *     var mapper = JacksonUtil.defaultMapperBuilder()
   *         .addMixIn(Foo.class, FooMixin.class)
   *         .build();
   *     JacksonUtil.setDefaultObjectMapper(mapper);
   *     return mapper;
   *   }
   * }
   * }</pre>
   *
   * @return a pre-configured builder.
   */
  public static JsonMapper.Builder defaultMapperBuilder() {
    return JsonMapper.builder()
        .changeDefaultPropertyInclusion(incl ->
            incl.withValueInclusion(JsonInclude.Include.NON_NULL))
        .addModule(PERMISSIVE_DATETIME_MODULE);
  }

  /**
   * Replaces the default object mapper used by all utility methods in this class. Typically called
   * once during application startup after building a customized mapper via
   * {@link #defaultMapperBuilder()}.
   *
   * @param objectMapper the customized object mapper to use.
   */
  public static void setDefaultObjectMapper(ObjectMapper objectMapper) {
    OBJECT_MAPPER = objectMapper;
  }

  /**
   * Deserializes a JSON string into an object of the given type.
   *
   * @param json the JSON string.
   * @param clazz the target class.
   * @param <T> the target type.
   * @return the deserialized object.
   */
  public static <T> T jsonToObject(String json, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Serializes an object into a compact JSON string.
   *
   * @param object the object to serialize.
   * @param <T> the object type.
   * @return the JSON string.
   */
  public static <T> String objectToJson(T object) {
    try {
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Serializes an object into a pretty-printed JSON string using 2-space indentation
   * and LF line endings for both objects and arrays.
   *
   * @param object the object to serialize.
   * @param <T> the object type.
   * @return the pretty-printed JSON string.
   */
  public static <T> String objectToPrettyJson(T object) {
    try {
      return OBJECT_MAPPER.writer().with(PRETTY_PRINTER).writeValueAsString(object);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Converts an object into a {@link JsonNode} tree representation.
   *
   * @param object the object to convert.
   * @return the JSON tree.
   */
  public static JsonNode objectToTree(Object object) {
    return OBJECT_MAPPER.valueToTree(object);
  }

  /**
   * Parses a JSON string into a {@link JsonNode} tree.
   *
   * @param json the JSON string.
   * @return the JSON tree.
   */
  public static JsonNode jsonToTree(String json) {
    try {
      return OBJECT_MAPPER.readTree(json);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Reads a JSON file from the classpath and parses it into a {@link JsonNode} tree.
   *
   * @param jsonFileNameInClassPath the classpath resource name.
   * @return the JSON tree.
   */
  public static JsonNode fileToTree(String jsonFileNameInClassPath) {
    try {
      return OBJECT_MAPPER.readTree(inputStream(jsonFileNameInClassPath));
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Reads a JSON {@link File} and parses it into a {@link JsonNode} tree.
   *
   * @param file the JSON file.
   * @return the JSON tree.
   */
  public static JsonNode fileToTree(File file) {
    try {
      return OBJECT_MAPPER.readTree(file);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Converts a {@link JsonNode} tree into an object of the given type.
   *
   * @param tree the JSON tree.
   * @param clazz the target class.
   * @param <T> the target type.
   * @return the deserialized object.
   */
  public static <T> T treeToObject(JsonNode tree, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.treeToValue(tree, clazz);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Converts a {@link JsonNode} tree into an object of the given generic type, e.g.
   * {@code List<Foo>}.
   *
   * @param tree the JSON tree.
   * @param reference the target generic type reference.
   * @param <T> the target type.
   * @return the deserialized object.
   */
  public static <T> T treeToObject(JsonNode tree, TypeReference<T> reference) {
    try {
      return OBJECT_MAPPER.treeToValue(tree, reference);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Converts a source object into the target type by serializing and deserializing through the
   * ObjectMapper's type conversion. Useful for converting between DTOs or from a {@link Map} into
   * a typed object.
   *
   * @param source the source object.
   * @param targetType the target class.
   * @param <T> the target type.
   * @return the converted object.
   */
  public static <T> T convertValue(Object source, Class<T> targetType) {
    return OBJECT_MAPPER.convertValue(source, targetType);
  }

  /**
   * Merges a partial JSON payload onto an existing object, returning the updated instance. Intended
   * for PATCH-style operations where only the supplied fields are overwritten.
   *
   * @param target the existing object to update (modified in place for mutable types).
   * @param patchJson the partial JSON containing only the fields to update.
   * @param <T> the object type.
   * @return the updated object.
   */
  public static <T> T merge(T target, String patchJson) {
    try {
      return OBJECT_MAPPER.readerForUpdating(target).readValue(patchJson);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Retrieves the contents of the text file from the classpath, and then deserialize the read value
   * to the requested class.
   *
   * @param jsonFileNameInClassPath the name of the text file in the class-path with valid json
   *     content.
   * @param clazz Requested class type to deserialize the json into.
   * @param <T> The object to be returned.
   * @return The deserialized class instance.
   */
  public static <T> T fileToObject(String jsonFileNameInClassPath, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(inputStream(jsonFileNameInClassPath), clazz);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Deserializes an {@link InputStream} of JSON content into an object of the given type.
   *
   * @param inputStream the JSON input stream.
   * @param clazz the target class.
   * @param <T> the target type.
   * @return the deserialized object.
   */
  public static <T> T streamToObject(InputStream inputStream, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(inputStream, clazz);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Deserializes a JSON string into a generic type described by the given {@link TypeReference}.
   *
   * @param json the JSON string.
   * @param reference the target type reference.
   * @param <T> the target type.
   * @return the deserialized object.
   */
  public static <T> T jsonToTypeReference(String json, TypeReference<T> reference) {
    try {
      return OBJECT_MAPPER.readValue(json, reference);
    } catch (JacksonException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Deserializes a JSON string into a {@code Map<String, Object>}.
   *
   * @param json the JSON string.
   * @return the deserialized map.
   */
  public static Map<String, Object> jsonToMap(String json) {
    return jsonToTypeReference(json, new TypeReference<HashMap<String, Object>>() {});
  }

  /**
   * Converts an object into a {@link Map} representation preserving field order.
   *
   * @param object the source object.
   * @return a map of field names to values.
   */
  public static Map<String, Object> objectToMap(Object object) {
    return OBJECT_MAPPER.convertValue(object, new TypeReference<LinkedHashMap<String, Object>>() {});
  }

  /**
   * Locate the text file in the classpath, read and return its contents as a UTF_8 string.
   *
   * @param textFileNameInClassPath the name of the text file in the class path.
   * @return the contents of the requested file.
   */
  public static String contents(String textFileNameInClassPath) {
    return contents(inputStream(textFileNameInClassPath));
  }

  /**
   * Assuming the input stream points to a text file, read and return its contents as a UTF_8
   * string.
   *
   * @param inputStream Input stream of a text file.
   * @return the contents of the requested input stream.
   */
  public static String contents(InputStream inputStream) {
    try {
      var result = new ByteArrayOutputStream();
      var buffer = new byte[1024];
      for (int length; (length = inputStream.read(buffer)) != -1; ) {
        result.write(buffer, 0, length);
      }
      return result.toString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Opens an {@link InputStream} for a classpath resource, throwing if not found.
   *
   * @param textFileNameInClassPath the classpath resource name.
   * @return the input stream.
   */
  public static InputStream inputStream(String textFileNameInClassPath) {
    var cl = Thread.currentThread().getContextClassLoader();
    var inputStream = cl.getResourceAsStream(textFileNameInClassPath);
    if (inputStream == null) {
      throw new IllegalArgumentException(
          "Resource not found on classpath: " + textFileNameInClassPath);
    }
    return inputStream;
  }

  static class PermissiveDateTimeDeserializer extends ValueDeserializer<OffsetDateTime> {

    private static final DateTimeFormatter FORMATTER =
        new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("yyyy-MM-dd")
            .optionalStart()
            .appendPattern("['T'][' ']HH[:mm[:ss]]")
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .optionalEnd()
            .optionalStart().appendPattern("XXX").optionalEnd()
            .optionalStart().appendPattern("XX").optionalEnd()
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
            .toFormatter(Locale.ENGLISH);

    @Override
    public OffsetDateTime getNullValue(DeserializationContext ctxt) {
      return null;
    }

    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctx) {

      JsonToken token = p.currentToken();

      if (token == JsonToken.VALUE_NUMBER_INT) {
        return toOffsetDateTime(p.getLongValue());
      }

      if (token != JsonToken.VALUE_STRING) {
        return (OffsetDateTime) ctx.handleUnexpectedToken(OffsetDateTime.class, p);
      }

      String txt = p.getString();
      if (txt == null) {
        return null;
      }

      String s = txt.trim();
      if (s.isEmpty()) {
        return null;
      }

      if (isNumeric(s)) {
        try {
          return toOffsetDateTime(Long.parseLong(s));
        } catch (NumberFormatException ex) {
          // too big for long -> fall through to formatter and let it fail
        }
      }

      return OffsetDateTime.parse(s, FORMATTER);
    }

    private static OffsetDateTime toOffsetDateTime(long epochMillis) {
      return OffsetDateTime.ofInstant(Instant.ofEpochMilli(epochMillis),
          ZoneOffset.UTC);
    }

    /** Test for [+|-]?[0-9]+ without regex allocation. */
    private static boolean isNumeric(String s) {
      var len = s.length();
      if (len == 0) {
        return false;
      }
      var i = 0;
      var c = s.charAt(0);
      if (c == '+' || c == '-') {
        if (len == 1) return false;
        i = 1;
      }
      for (; i < len; i++) {
        if (!Character.isDigit(s.charAt(i))) return false;
      }
      return true;
    }
  }
}
