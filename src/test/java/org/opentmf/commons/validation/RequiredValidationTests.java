package org.opentmf.commons.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.opentmf.commons.util.JacksonUtil.fileToObject;
import static org.opentmf.commons.validation.ValidationUtil.ensureValid;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.opentmf.commons.validation.constraints.Required;
import org.opentmf.tmf.model.Characteristic;

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
