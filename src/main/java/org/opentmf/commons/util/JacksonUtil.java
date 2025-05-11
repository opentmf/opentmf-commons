package org.opentmf.commons.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.Locale;
import java.util.Map;
import lombok.Generated;

/**
 * @author Gökhan Demir
 */
public final class JacksonUtil {

  private static final JavaTimeModule JAVA_TIME_MODULE = new JavaTimeModule();
  private static final ObjectMapper OBJECT_MAPPER;

  static {
    JAVA_TIME_MODULE.addDeserializer(OffsetDateTime.class, new PermissiveDateTimeDeserializer());

    OBJECT_MAPPER =
        new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(JAVA_TIME_MODULE);
  }

  @Generated
  private JacksonUtil() {
    throw new UnsupportedOperationException(
        "JacksonUtil is a utility library with static methods, " + "hence cannot be instantiated.");
  }

  /**
   * Returns the default initialized objectMapper, which is capable of serializing and deserializing
   * OffsetDateTime in a safe way, disables writing dates as long values, and does not fail on
   * unknown properties. <br>
   * <br>
   *
   * <p><strong>Hint:</strong>Callers can retrieve this objectMapper and then add their mix ins if
   * necessary, and then can register a primary bean of type ObjectMapper with the enriched one.
   * Since the utility methods work with this static object mapper, the enriched object mapper must
   * not clone this one or the utility methods in this class might not behave as expected.
   *
   * @return the default object mapper.
   */
  public static ObjectMapper getDefaultObjectMapper() {
    return OBJECT_MAPPER;
  }

  public static <T> T jsonToObject(String json, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> String objectToJson(T object) {
    try {
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> String objectToPrettyJson(T object) {
    try {
      return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static JsonNode objectToTree(Object object) {
    return OBJECT_MAPPER.valueToTree(object);
  }

  public static JsonNode jsonToTree(String json) {
    try {
      return OBJECT_MAPPER.readTree(json);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static JsonNode fileToTree(String jsonFileNameInClassPath) {
    try {
      return OBJECT_MAPPER.readTree(inputStream(jsonFileNameInClassPath));
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static JsonNode fileToTree(File file) {
    try {
      return OBJECT_MAPPER.readTree(file);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> T treeToObject(JsonNode tree, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.treeToValue(tree, clazz);
    } catch (JsonProcessingException e) {
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
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> T streamToObject(InputStream inputStream, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(inputStream, clazz);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> T jsonToTypeReference(String json, TypeReference<T> reference) {
    try {
      return OBJECT_MAPPER.readValue(json, reference);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static Map<String, Object> jsonToMap(String json) {
    return jsonToTypeReference(json, new TypeReference<HashMap<String, Object>>() {});
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

  public static InputStream inputStream(String textFileNameInClassPath) {
    return ClassLoader.getSystemResourceAsStream(textFileNameInClassPath);
  }

  static class PermissiveDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    /* ---------- 1. The flexible ISO-ish formatter (unchanged) ---------- */
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

    /* ---------- 2. Tell Jackson what to return for a JSON null ---------- */
    @Override
    public OffsetDateTime getNullValue(DeserializationContext ctxt) {
      return null;                         // same semantic as before
    }

    /* ---------- 3. Core deserialization logic (no VALUE_NULL branch) ---- */
    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException {

      JsonToken token = p.currentToken();

      /* a) Numeric token  -> epoch-milliseconds ----------------------- */
      if (token == JsonToken.VALUE_NUMBER_INT) {
        return toOffsetDateTime(p.getLongValue());
      }

      /* b) Expect a string from here on ------------------------------ */
      if (token != JsonToken.VALUE_STRING) {
        return (OffsetDateTime) ctx.handleUnexpectedToken(OffsetDateTime.class, p);
      }

      String txt = p.getText();
      if (txt == null) {                   // defensive; shouldn’t happen
        return null;
      }

      String s = txt.trim();
      if (s.isEmpty()) {                   // blank string -> null
        return null;
      }

      /* b1) Purely numeric string  -> epoch-milliseconds -------------- */
      if (isNumeric(s)) {
        try {
          return toOffsetDateTime(Long.parseLong(s));
        } catch (NumberFormatException ex) {
          // too big for long -> fall through to formatter and let it fail
        }
      }

      /* b2) Otherwise parse with the formatter ----------------------- */
      return OffsetDateTime.parse(s, FORMATTER);
    }

    /* ---------- 4. Helpers -------------------------------------------- */

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
  }}
