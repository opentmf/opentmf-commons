package org.opentmf.commons.validation;

import static org.opentmf.commons.util.JacksonUtil.objectToTree;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    if (obj == null) {
      return true;
    }
    var thisRequired = findRequired(obj.getClass());
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

  /**
   * Finds @Required on the class itself, then on directly implemented interfaces.
   */
  private Required findRequired(Class<?> clazz) {
    var declared = clazz.getDeclaredAnnotation(Required.class);
    if (declared != null) {
      return declared;
    }
    for (Class<?> iface : clazz.getInterfaces()) {
      var fromIface = iface.getDeclaredAnnotation(Required.class);
      if (fromIface != null) {
        return fromIface;
      }
    }
    return null;
  }

  private String jsonName(Object obj, String name) {
    var field = getField(obj.getClass(), name);
    if (field != null) {
      var jsonProperty = field.getAnnotation(JsonProperty.class);
      if (jsonProperty != null && !jsonProperty.value().isEmpty()) {
        return jsonProperty.value();
      }
    }
    var fromMethod = getJsonPropertyFromInterfaces(obj.getClass(), name);
    if (fromMethod != null) {
      return fromMethod;
    }
    return name;
  }

  /**
   * Looks for @JsonProperty on the getter method in implemented interfaces.
   */
  private String getJsonPropertyFromInterfaces(Class<?> clazz, String fieldName) {
    String suffix = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    String[] candidates = {"get" + suffix, "is" + suffix};
    for (Class<?> iface : clazz.getInterfaces()) {
      for (String getter : candidates) {
        var value = findJsonPropertyOnMethod(iface, getter);
        if (value != null) {
          return value;
        }
      }
      for (Class<?> parentIface : iface.getInterfaces()) {
        for (String getter : candidates) {
          var value = findJsonPropertyOnMethod(parentIface, getter);
          if (value != null) {
            return value;
          }
        }
      }
    }
    return null;
  }

  private String findJsonPropertyOnMethod(Class<?> iface, String methodName) {
    try {
      Method method = iface.getDeclaredMethod(methodName);
      var jsonProperty = method.getAnnotation(JsonProperty.class);
      if (jsonProperty != null && !jsonProperty.value().isEmpty()) {
        return jsonProperty.value();
      }
    } catch (NoSuchMethodException ignored) {
      // getter not declared on this interface
    }
    return null;
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
