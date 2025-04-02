package org.opentmf.commons.validation;

import static org.opentmf.commons.util.JacksonUtil.objectToTree;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.opentmf.commons.validation.constraints.Required;

/**
 * Validate only and only if the initialized @Required belongs to the declaring class, not to a
 * parent class. Also, since we are checking the field values using the JsonNode, we are also
 * finding the potential JsonProperty value of a field that will be checked.
 *
 * @author Gokhan Demir
 */
public class RequiredValidator implements ConstraintValidator<Required, Object> {

  private Required required;

  @Override
  public void initialize(Required required) {
    this.required = required;
  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    var thisRequired = obj.getClass().getDeclaredAnnotation(Required.class);
    if (thisRequired == null || thisRequired != required) {
      return true;
    }

    Set<String> nullFields = new HashSet<>();
    var jsonNode = objectToTree(obj);
    for (String field : required.fields()) {
      if (jsonNode.get(jsonName(obj, field)) == null) {
        nullFields.add(field);
      }
    }
    if (!nullFields.isEmpty()) {
      var ctx = context.unwrap(HibernateConstraintValidatorContext.class);
      ctx.disableDefaultConstraintViolation();
      ctx.addMessageParameter("nullFields", nullFields)
          .buildConstraintViolationWithTemplate(required.message())
          .addConstraintViolation();
      return false;
    }
    return true;
  }

  private String jsonName(Object obj, String name) {
    var field = getField(obj.getClass(), name);
    if (field == null) {
      return name;
    }
    var jsonProperty = field.getAnnotation(JsonProperty.class);
    if (jsonProperty == null || jsonProperty.value().isEmpty()) {
      return name;
    }
    return jsonProperty.value();
  }

  private Field getField(Class<?> clazz, String name) {
    if (clazz == null) {
      return null;
    }
    for (Field f : clazz.getDeclaredFields()) {
      if (name.equals(f.getName())) {
        return f;
      }
    }
    return getField(clazz.getSuperclass(), name);
  }
}
