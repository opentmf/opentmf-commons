package com.pia.commons.validation;

import static com.pia.commons.validation.RegexValidations.SAFE_TEXT;
import static com.pia.commons.validation.ValidationUtil.validate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pia.commons.validation.constraints.SafeText;
import com.pia.tmf.model.Characteristic;
import com.pia.tmf.model.EntityRef;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Gokhan Demir
 */
class SafeTextValidationTests {

  @Test
  void testSafeTextValidation_withValidValue_doesNotThrowException() {
    var entityRef = new EntityRef();
    entityRef.setId("test_string-value-with-numeric_1234");
    entityRef.setName(UUID.randomUUID().toString());

    var violationSet = validate(entityRef);
    assertThat(violationSet).isEmpty();
  }

  @Test
  void testSafeTextValidation_withInvalidValue_doesNotThrowException() {
    var entityRef = new EntityRef();
    entityRef.setId("invalid-string_with!");
    entityRef.setName("invalid_<string>");

    var violations = validate(entityRef);
    assertThat(violations)
        .hasSize(2)
        .allMatch(violation -> violation.getMessage().equals("Only alphanumeric characters, minus, "
            + "plus, space, asterisk, slash, dot, colon and underscore are allowed."));
  }

  @Test
  void testSafeTextValidation_withObjectValue_doesNotThrowViolation() {
    var characteristic = new Characteristic();
    characteristic.setName("validString");
    characteristic.setValue(new Object());

    var violationSet = validate(characteristic);
    assertThat(violationSet).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "filename.txt",
      "file_name",
      "file-name",
      "file name",
      "application/pdf"
  })
  void testSafeTextValidation_withValidStrings_matchesText(String s) {
    assertTrue(SAFE_TEXT.matches(s));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "https://www.address.com/<>",
      "method()",
      "script:<>",
      "{}",
      "[]"
  })
  void testSafeTextValidation_withDangerousStrings_fails(String s) {
    assertFalse(SAFE_TEXT.matches(s));
  }

  @Getter
  @Setter
  static class TestModel {

    @SafeText(message = "Test message")
    private String name;
  }

  @Test
  void testSafeTextValidation_withInvalidString_failsWithCustomMessage() {
    var testModel = new TestModel();
    testModel.setName("<Invalid>");
    var violationSet = validate(testModel);
    assertEquals(1, violationSet.size());
    violationSet.forEach(violation -> assertEquals("Test message", violation.getMessage()));
  }
}