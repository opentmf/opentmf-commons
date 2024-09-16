package com.pia.commons.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.pia.commons.validation.ValidationUtil;
import com.pia.commons.validation.constraints.SafeJsonPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

/**
 * @author Gokhan Demir
 */
class SafeJsonPathValidationTests {

  @Test
  void testSafeJsonPath_withDisallowedText_throwsException() {
    var sample = new Sample("$.store.^book[?(@.price > $.expensive)]");
    assertThrows(Exception.class, () -> ValidationUtil.ensureValid(sample));
  }

  @Test
  void testSafeJsonPath_withValidText_permitsUse() {
    var sample = new Sample("$.store.book[?(@.price > $.expensive)]");
    assertDoesNotThrow(() -> ValidationUtil.ensureValid(sample));
  }

  @Test
  void testSafeJsonPath_withNullText_permitsUse() {
    var sample = new Sample(null);
    assertDoesNotThrow(() -> ValidationUtil.ensureValid(sample));
  }

  @RequiredArgsConstructor
  @Getter
  private static class Sample {

    @SafeJsonPath
    private final String jsonPath;
  }

}
