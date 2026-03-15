package org.opentmf.commons.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

  @Test
  void testRequired_withNullObject_validatesSuccessfully() {
    var validator = new RequiredValidator();
    assertTrue(validator.isValid(null, null));
  }

  @Test
  void testRequired_withRequiredOnInterface_andMissingField_throwsException() {
    var obj = new InterfaceImpl();
    assertThrows(ConstraintViolationException.class, () -> ensureValid(obj));
  }

  @Test
  void testRequired_withRequiredOnInterface_andFieldPresent_validatesSuccessfully() {
    var obj = new InterfaceImpl();
    obj.setTitle("a title");
    assertDoesNotThrow(() -> ensureValid(obj));
  }

  @Test
  void testRequired_withJsonPropertyOnInterfaceGetter_resolvesJsonName() {
    var obj = new JsonPropertyInterfaceImpl();
    assertThrows(ConstraintViolationException.class, () -> ensureValid(obj));
  }

  @Test
  void testRequired_withJsonPropertyOnInterfaceGetter_andFieldPresent_validatesSuccessfully() {
    var obj = new JsonPropertyInterfaceImpl();
    obj.setDisplayName("hello");
    assertDoesNotThrow(() -> ensureValid(obj));
  }

  @Test
  void testRequired_withJsonPropertyOnParentInterfaceGetter_resolvesJsonName() {
    var obj = new ParentInterfaceImpl();
    assertThrows(ConstraintViolationException.class, () -> ensureValid(obj));
  }

  @Test
  void testRequired_withJsonPropertyOnParentInterfaceGetter_andFieldPresent_validatesSuccessfully() {
    var obj = new ParentInterfaceImpl();
    obj.setCode("C01");
    assertDoesNotThrow(() -> ensureValid(obj));
  }

  @Test
  void testRequired_withBooleanIsGetter_onInterface_resolvesJsonName() {
    var obj = new BooleanInterfaceImpl();
    obj.setActive(true);
    assertDoesNotThrow(() -> ensureValid(obj));
  }

  @Test
  void testRequired_withEmptyJsonPropertyOnInterfaceGetter_fallsBackToFieldName() {
    var obj = new EmptyJsonPropertyInterfaceImpl();
    obj.setLabel("ok");
    assertDoesNotThrow(() -> ensureValid(obj));
  }

  @Test
  void testRequired_withNoMatchingGetterOnInterface_fallsBackToFieldName() {
    var obj = new NoGetterInterfaceImpl();
    obj.setDescription("desc");
    assertDoesNotThrow(() -> ensureValid(obj));
  }

  // --- test models: class-level @Required ---

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

  // --- test models: interface-level @Required ---

  @Required(fields = {"title"})
  interface HasTitle {
    String getTitle();
  }

  @Getter
  @Setter
  static class InterfaceImpl implements HasTitle {
    private String title;
  }

  // --- test models: @JsonProperty on interface getter ---

  @Required(fields = {"displayName"})
  interface HasDisplayName {
    @JsonProperty("display_name")
    String getDisplayName();
  }

  @Getter
  @Setter
  static class JsonPropertyInterfaceImpl implements HasDisplayName {
    private String displayName;
  }

  // --- test models: @JsonProperty on parent interface getter ---

  interface GrandParentInterface {
    @JsonProperty("code_value")
    String getCode();
  }

  @Required(fields = {"code"})
  interface ChildInterface extends GrandParentInterface {
  }

  @Getter
  @Setter
  static class ParentInterfaceImpl implements ChildInterface {
    private String code;
  }

  // --- test models: boolean isXxx() getter on interface ---

  @Required(fields = {"active"})
  interface HasActive {
    @JsonProperty("is_active")
    boolean isActive();
  }

  @Getter
  @Setter
  static class BooleanInterfaceImpl implements HasActive {
    private boolean active;
  }

  // --- test models: empty @JsonProperty on interface getter ---

  @Required(fields = {"label"})
  interface HasLabel {
    @JsonProperty
    String getLabel();
  }

  @Getter
  @Setter
  static class EmptyJsonPropertyInterfaceImpl implements HasLabel {
    private String label;
  }

  // --- test models: no matching getter on interface ---

  @Required(fields = {"description"})
  interface HasDescription {
  }

  @Getter
  @Setter
  static class NoGetterInterfaceImpl implements HasDescription {
    private String description;
  }
}
