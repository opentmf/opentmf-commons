package org.opentmf.commons.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.opentmf.commons.util.JacksonUtil.fileToObject;
import static org.opentmf.commons.validation.ValidationUtil.ensureValid;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentmf.tmf.model.CharacteristicList;
import org.opentmf.tmf.model.EventSubscriptionInput;

/**
 * @author Gokhan Demir
 */
@Slf4j
class ValidationUtilTests {

  private static Stream<Arguments> validData() {
    return Stream.of(
        Arguments.of("json/characteristics_valid.json", CharacteristicList.class),
        Arguments.of("json/event_subscription_valid.json", EventSubscriptionInput.class),
        Arguments.of("json/event_subscription_valid_2.json", EventSubscriptionInput.class)
    );
  }
  
  private static Stream<Arguments> invalidData() {
    return Stream.of(
        Arguments.of("json/characteristics_invalid.json", CharacteristicList.class),
        Arguments.of("json/event_subscription_invalid.json", EventSubscriptionInput.class)
    );
  }

  @ParameterizedTest
  @MethodSource("validData")
  void testValidate_withValidData_validatesSuccessfully(String file, Class<?> clazz) {
    var obj = fileToObject(file, clazz);
    Set<ConstraintViolation<Object>> violations = ValidationUtil.validate(obj);
    assertEquals(0, violations.size());
  }

  @ParameterizedTest
  @MethodSource("validData")
  void testEnsureValid_withValidData_validatesSuccessfully(String file, Class<?> clazz) {
    var obj = fileToObject(file, clazz);
    assertDoesNotThrow(() -> ensureValid(obj));
  }

  @ParameterizedTest
  @MethodSource("invalidData")
  void testValidate_withInvalidData_detectsValidationErrors(String file, Class<?> clazz) {
    var obj = fileToObject(file, clazz);
    Set<ConstraintViolation<Object>> violations = ValidationUtil.validate(obj);
    assertFalse(violations.isEmpty());
  }

  @Test
  void testEnsureValid_withInvalidData_throwsException() {
    var obj = fileToObject("json/characteristics_invalid.json", CharacteristicList.class);
    var e = assertThrows(ConstraintViolationException.class, () -> ensureValid(obj));
    assertTrue(e.getMessage().startsWith("Object has 2 validation errors."));
    assertTrue(e.getMessage().contains("characteristics[0].name --> Only alphanumeric characters"));
    assertTrue(e.getMessage().contains("characteristics[0].valueType --> size must be between"));
  }

  @Test
  void testRequired_withValidData_returnsValidResult() {
    var obj = fileToObject("json/characteristics_valid.json", CharacteristicList.class);
    Set<ConstraintViolation<Object>> violations = ValidationUtil.validate(obj);
    assertTrue(violations.isEmpty());
  }

  @Test
  void testRequired_withMissingRequiredFields_throwsException() {
    var obj = fileToObject("json/characteristics_missing_required.json", CharacteristicList.class);
    Set<ConstraintViolation<Object>> violations = ValidationUtil.validate(obj);
    assertFalse(violations.isEmpty());
  }

  @Test
  void testRequired_withMissingRequiredFields_producesExpectedErrorText() {
    var obj = fileToObject("json/characteristics_missing_required.json", CharacteristicList.class);
    Exception e = assertThrows(ConstraintViolationException.class, () -> ensureValid(obj));
    String message = e.getMessage();
    assertTrue(message.contains("Missing values for the required [name]"));
    assertTrue(message.contains("Missing values for the required [value]"));
    assertTrue(message.contains("Missing values for the required [name, value]"));
    assertTrue(message.contains("characteristics[1].value --> must not be null"));
    Arrays.stream(message.split("Violation")).forEach(log::debug);
  }
}
