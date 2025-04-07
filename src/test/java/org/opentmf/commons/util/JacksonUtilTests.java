package org.opentmf.commons.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.opentmf.commons.util.JacksonUtil.contents;
import static org.opentmf.commons.util.JacksonUtil.fileToObject;
import static org.opentmf.commons.util.JacksonUtil.fileToTree;
import static org.opentmf.commons.util.JacksonUtil.getDefaultObjectMapper;
import static org.opentmf.commons.util.JacksonUtil.inputStream;
import static org.opentmf.commons.util.JacksonUtil.jsonToMap;
import static org.opentmf.commons.util.JacksonUtil.jsonToObject;
import static org.opentmf.commons.util.JacksonUtil.jsonToTree;
import static org.opentmf.commons.util.JacksonUtil.jsonToTypeReference;
import static org.opentmf.commons.util.JacksonUtil.objectToJson;
import static org.opentmf.commons.util.JacksonUtil.objectToPrettyJson;
import static org.opentmf.commons.util.JacksonUtil.objectToTree;
import static org.opentmf.commons.util.JacksonUtil.streamToObject;
import static org.opentmf.commons.util.JacksonUtil.treeToObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentmf.tmf.model.Addressable;
import org.opentmf.tmf.model.TimePeriod;

/**
 * @author Gokhan Demir
 */
class JacksonUtilTests {

  private static final String JSON =
      "{ \"id\": \"305f2215715f\", \"href\": \"https://host/Attachment/305f2215715f\" }";

  private static final String INVALID_JSON =
      "{ \"id: \"305f2215715f\", \"href\": \"https://host/Attachment/305f2215715f\" }";

  static Stream<Arguments> offsetDateTimeConversionExpectations() {
    return Stream.of(
        Arguments.of("2007-12-03T10:15:29+01:00", "2007-12-03T10:15:29+01:00"),
        Arguments.of("2007-12-03T10:15:29+0100", "2007-12-03T10:15:29+01:00"),
        Arguments.of("2007-12-03T10:15:29+00:00", "2007-12-03T10:15:29Z"),
        Arguments.of("2007-12-03T10:15+00:00", "2007-12-03T10:15Z"),
        Arguments.of("2007-12-03 10:15+00:00", "2007-12-03T10:15Z"),
        Arguments.of("2007-12-03 10:15+01:00", "2007-12-03T10:15+01:00"),
        Arguments.of("2007-12-03", "2007-12-03T00:00Z"),
        Arguments.of("2007-12-03T10:15:29.123+00:00", "2007-12-03T10:15:29.123Z")
    );
  }

  @ParameterizedTest
  @MethodSource("offsetDateTimeConversionExpectations")
  void testOffsetDateTimeDeserialization_withSupportedFormat_returnsExpectedResult(
      String input, String expected) throws JsonProcessingException {
    var json = "{\"time\": \"" + input + "\"}";
    var model = JacksonUtil.getDefaultObjectMapper().readValue(json, SomeModel.class);
    Assertions.assertEquals(expected, model.getTime().toString());
  }

  @Getter @Setter
  static class SomeModel {
    private OffsetDateTime time;
  }

  @Test
  void test_getDefaultObjectMapper_returnsValidObject() {
    Assertions.assertNotNull(JacksonUtil.getDefaultObjectMapper());
  }

  @Test
  void test_jsonToObject_withValidData_returnsValidObject() {
    Assertions.assertNotNull(jsonToObject(JSON, Addressable.class));
  }

  @Test
  void test_treeToObject_withValidData_returnsValidObject() {
    var originalObject = jsonToObject(JSON, Addressable.class);
    var tree = objectToTree(originalObject);
    assertThat(originalObject)
        .usingRecursiveComparison()
        .isEqualTo(treeToObject(tree, Addressable.class));
  }

  @Test
  void test_jsonToTree_withValidData_returnsValidObject() {
    var tree = jsonToTree(JSON);
    assertThat(jsonToObject(JSON, Addressable.class))
        .usingRecursiveComparison()
        .isEqualTo(treeToObject(tree, Addressable.class));
  }

  @Test
  void test_treeToObject_withValidDataButInvalidClass_throwsError() {
    var originalObject = jsonToObject(JSON, Addressable.class);
    var tree = (ObjectNode) objectToTree(originalObject);
    assertThrows(IllegalArgumentException.class, () -> treeToObject(tree, WillNotSerialize.class));
  }

  @Test
  void test_jsonToTree_withInValidData_throwsError() {
    assertThrows(IllegalArgumentException.class, () -> jsonToTree(INVALID_JSON));
  }

  @Test
  void test_fileToTree_withInValidData_throwsError() {
    assertThrows(IllegalArgumentException.class, () -> fileToTree("json/invalid.json"));
  }

  @Test
  void test_fileToTree_withInValidData_returnsValidObject() {
    var tree = fileToTree("json/valid.json");
    assertThat(treeToObject(tree, Addressable.class))
        .usingRecursiveComparison()
        .isEqualTo(fileToObject("json/valid.json", Addressable.class));
  }

  @Test
  void test_fileToTree_withFileContainingInvalidData_throwsError() throws URISyntaxException {
    var url = getClass().getClassLoader().getResource("json/invalid.json");
    assertNotNull(url);
    var file = new File(url.toURI());
    assertThrows(IllegalArgumentException.class, () -> fileToTree(file));
  }

  @Test
  void test_fileToTree_withFileContainingValidData_returnsValidObject() throws URISyntaxException {
    var url = getClass().getClassLoader().getResource("json/valid.json");
    assertNotNull(url);
    var file = new File(url.toURI());
    var tree = fileToTree(file);
    assertThat(treeToObject(tree, Addressable.class))
        .usingRecursiveComparison()
        .isEqualTo(fileToObject("json/valid.json", Addressable.class));
  }

  @Getter
  @Setter
  private static class WillNotSerialize {
    @JsonProperty("id")
    private String id;

    @JsonProperty("id")
    private String href;
  }

  @Test
  void test_streamToObject_withValidData_returnsValidObject() {
    Assertions.assertNotNull(
        streamToObject(inputStream("json/addressable_valid.json"), Addressable.class));
  }

  @Test
  void test_jsonToObject_withInvalidData_throwsException() {
    var e = assertThrows(Exception.class, () -> jsonToObject(INVALID_JSON, Addressable.class));
    Assertions.assertInstanceOf(JsonProcessingException.class, ExceptionUtils.getRootCause(e));
  }

  @Test
  void test_fileToObject_withValidData_returnsValidObject() {
    Assertions.assertNotNull(fileToObject("json/addressable_valid.json", Addressable.class));
  }

  @Test
  void test_fileToObject_withInvalidData_throwsException() {
    Exception e =
        assertThrows(
            Exception.class,
            () -> fileToObject("json/addressable_invalid.json", Addressable.class));
    Assertions.assertInstanceOf(JsonProcessingException.class, ExceptionUtils.getRootCause(e));
  }

  @Test
  void test_contents_withValidData_returnsValidObject() {
    var json = JacksonUtil.contents("json/addressable_valid.json");
    Assertions.assertNotNull(json);
  }

  @Test
  void test_contents_withBadInputStream_throwsException() throws IOException {
    try (var inputStream = mock(InputStream.class)) {
      when(inputStream.read(any())).thenThrow(IOException.class);
      var e = assertThrows(IllegalArgumentException.class, () -> JacksonUtil.contents(inputStream));
      Assertions.assertInstanceOf(IOException.class, ExceptionUtils.getRootCause(e));
    }
  }

  @Test
  void test_streamToObject_withBadInputStream_throwsException() throws IOException {
    try (var inputStream = mock(InputStream.class)) {
      when(inputStream.read(any())).thenThrow(IOException.class);
      var e =
          assertThrows(
              IllegalArgumentException.class,
              () -> JacksonUtil.streamToObject(inputStream, Addressable.class));
      Assertions.assertInstanceOf(IOException.class, ExceptionUtils.getRootCause(e));
    }
  }

  @Test
  void test_objectToJson_withValidData_returnsValidJson() {
    Object object = object();
    var json = objectToJson(object);
    Assertions.assertNotNull(json);
    Object reverseObject = jsonToObject(json, object.getClass());
    assertThat(object).usingRecursiveComparison().isEqualTo(reverseObject);
  }

  @Test
  void test_objectToPrettyJson_withValidData_returnsValidJson() {
    Object object = object();
    var json = objectToPrettyJson(object);
    Assertions.assertNotNull(json);
    Object reverseObject = jsonToObject(json, object.getClass());
    assertThat(object).usingRecursiveComparison().isEqualTo(reverseObject);
  }

  @Test
  void test_objectToJson_withMockObject_throwsException() {
    Object badObject = mock(Object.class);
    when(badObject.toString()).thenReturn(badObject.getClass().getName());
    getDefaultObjectMapper().enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    Exception e = assertThrows(Exception.class, () -> objectToJson(badObject));
    getDefaultObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    Assertions.assertInstanceOf(JsonProcessingException.class, ExceptionUtils.getRootCause(e));
  }

  @Test
  void test_objectToPrettyJson_withMockObject_throwsException() {
    Object badObject = mock(Object.class);
    when(badObject.toString()).thenReturn(badObject.getClass().getName());
    getDefaultObjectMapper().enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    Exception e = assertThrows(Exception.class, () -> objectToPrettyJson(badObject));
    getDefaultObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    Assertions.assertInstanceOf(JsonProcessingException.class, ExceptionUtils.getRootCause(e));
  }

  @Test
  void test_jsonToObjectList_withValidData_returnsList() {
    var json = contents("json/time_period_list.json");
    var timePeriods = jsonToTypeReference(json, new TypeReference<List<TimePeriod>>() {});
    Assertions.assertNotNull(timePeriods);
    assertEquals(2, timePeriods.size());
  }

  @Test
  void test_jsonToObjectList_withInvalidData_throwsException() {
    var json = contents("json/time_period.json");
    var typeReference = new TypeReference<List<TimePeriod>>() {};
    var e =
        assertThrows(
            IllegalArgumentException.class, () -> jsonToTypeReference(json, typeReference));
    Assertions.assertInstanceOf(JsonProcessingException.class, ExceptionUtils.getRootCause(e));
  }

  @Test
  void test_jsonToMap_withValidDate_returnsMap() {
    var json = contents("json/time_period.json");
    var map = jsonToMap(json);
    Assertions.assertNotNull(map);
    assertEquals(2, map.size());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "json/time_period_empty_date.json",
      "json/time_period_null_date.json"
  })
  void test_jsonToObject_withEmptyOrNullDateTime_deserializesDateTimeAsNull(String path) {
    var json = contents(path);
    var timePeriod = jsonToObject(json, TimePeriod.class);
    Assertions.assertNotNull(timePeriod);
    assertNull(timePeriod.getEndDateTime());
  }

  private static final String TEST_OBJECT_STRING =
      "{\"localDate\":\"1979-05-23\",\"offsetDateTime\":\"2024-07-04T12:20:02.900+0000\",\"name\":\"test\"}";

  @Test
  void testSerializeAndDeserializeObject_withLocalAndOffsetDateTime_returnsValidResult() {
    var testObject = assertDoesNotThrow(() -> jsonToObject(TEST_OBJECT_STRING, TestObject.class));
    assertNotNull(testObject);
    // assertEquals(TEST_OBJECT_STRING, objectToJson(testObject).replaceAll("\\s+", ""));
  }

  @Getter
  @Setter
  private static class TestObject {
    private LocalDate localDate;
    private OffsetDateTime offsetDateTime;
    private String name;
  }

  private Object object() {
    Addressable object = new Addressable();
    object.setHref(URI.create("https://host/Attachment/305f2215715f"));
    object.setId("305f2215715f");
    return object;
  }
}
