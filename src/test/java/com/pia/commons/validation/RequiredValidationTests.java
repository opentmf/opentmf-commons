package com.pia.commons.validation;

import static com.pia.commons.util.JacksonUtil.fileToObject;
import static com.pia.commons.validation.ValidationUtil.ensureValid;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pia.commons.validation.constraints.Required;
import com.pia.tmf.model.Characteristic;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

/**
 * @author Gokhan Demir
 */
class RequiredValidationTests {

  @Test
  void testRequired_withMissingParentRequired_validatesSuccessfully() {
    var obj = fileToObject("json/required_1.json", Characteristic.class);
    assertNotNull(obj);
    assertDoesNotThrow(() -> ensureValid(obj));
  }

  @Test
  void testRequired_withWrongFieldNameSpecifiedInSelfClass_throwsException() {
    var obj = new Sample();
    assertThrows(ConstraintViolationException.class, () -> ensureValid(obj));
  }

  @Test
  void testRequired_withEmptyJsonProperty_andWrongFieldNameSpecifiedInParentClass_validatesSuccessfully() {
    var obj = new Extended1();
    obj.setName("name");
    assertDoesNotThrow(() -> ensureValid(obj));
  }

  @Test
  void testRequired_withJsonProperty_andWrongFieldNameSpecifiedInParentClass_validatesSuccessfully() {
    var obj = new Extended2();
    obj.setName("name");
    assertDoesNotThrow(() -> ensureValid(obj));
  }

  @Test
  void testRequired_withoutJsonProperty_andWrongFieldNameSpecifiedInParentClass_validatesSuccessfully() {
    var obj = new Extended3();
    obj.setName("name");
    assertDoesNotThrow(() -> ensureValid(obj));
  }

  @Getter
  @Setter
  @Required(fields = {"nonExistentField"})
  private static class Sample {
    private String id;
  }

  @Getter
  @Setter
  @Required(fields = {"name"})
  private static class Extended1 extends Sample {

    @JsonProperty
    private String name;
  }

  @Getter
  @Setter
  @Required(fields = {"name"})
  private static class Extended2 extends Sample {

    @JsonProperty("name")
    private String name;
  }

  @Getter
  @Setter
  @Required(fields = {"name"})
  private static class Extended3 extends Sample {

    private String name;
  }
}
