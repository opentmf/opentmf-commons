package org.opentmf.commons.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.opentmf.commons.validation.RegexValidations.SAFE_TEXT;
import static org.opentmf.commons.validation.ValidationUtil.validate;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentmf.commons.validation.constraints.SafeText;
import org.opentmf.tmf.model.Characteristic;
import org.opentmf.tmf.model.EntityRef;

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
    entityRef.setId("invalid-string_with;");
    entityRef.setName("invalid_<string>");

    var violations = validate(entityRef);
    assertThat(violations)
        .hasSize(2)
        .allMatch(violation -> violation.getMessage().equals("Only letters, digits, spaces, and "
            + "the characters _ - + % * . , : / ? ! ( ) and apostrophes are allowed."));
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
      "application/pdf",
      "François",
      "Hervé Jr.",
      "Müller",
      "Łukasz",
      "İstanbul",
      "O'Brien",
      "l’Hôpital",
      "Smith, John",
      "100%",
      "why?",
      "wow!",
      "foo (bar)",
      "a/b/c"
  })
  void testSafeTextValidation_withValidStrings_matchesText(String s) {
    assertTrue(SAFE_TEXT.matches(s));
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "https://www.address.com/<>",
      "<script>alert(1)</script>",
      "script:<>",
      "{}",
      "[]",
      "\"quoted\"",
      "foo;bar",
      "key=value",
      "${var}",
      "`cmd`",
      "back\\slash",
      "pipe|chain",
      "tilde~here",
      "hash#tag",
      "at@sign",
      "caret^up"
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