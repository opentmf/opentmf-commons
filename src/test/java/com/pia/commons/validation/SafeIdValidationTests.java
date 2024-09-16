package com.pia.commons.validation;

import static com.pia.commons.validation.ValidationUtil.ensureValid;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.pia.commons.validation.constraints.SafeId;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Gokhan Demir
 */
class SafeIdValidationTests {

  @Test
  void testSafeId_withNullId_returnsValidResult() {
    assertDoesNotThrow(() -> ensureValid(new Sample(null)));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "",
      "A",
      "1",
      "1_A",
      "1-A",
      "1__A",
      "1--A",
      "1-_A",
      "1_-A"
  })
  void testSafeId_withValidId_returnsValidResult(String id) {
    assertDoesNotThrow(() -> ensureValid(new Sample(id)));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "*",
      "/",
      "<",
      ">",
      "?",
      "Ö",
      ".",
      " ",
      ";",
      ":",
      ","
  })
  void testSafeId_withInvalidId_throwsValidationException(String id) {
    var data = new Sample(id);
    Assertions.assertThrows(ConstraintViolationException.class,
        () -> ensureValid(data));
  }

  @RequiredArgsConstructor
  @Getter
  private static class Sample {

    @SafeId
    private final String id;
  }
}
