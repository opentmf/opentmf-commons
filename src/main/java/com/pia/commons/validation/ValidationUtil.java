package com.pia.commons.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import lombok.Generated;

public final class ValidationUtil {

  private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
  private static final Validator VALIDATOR = FACTORY.getValidator();

  @Generated
  private ValidationUtil() {
    throw new UnsupportedOperationException(
        "ValidationUtil is a utility class with only "
            + "static methods, therefore cannot be instantiated.");
  }

  /**
   * Validates all constraints on the object.
   *
   * @param object the object to validate.
   * @return constraint violations or an empty set if none.
   */
  public static Set<ConstraintViolation<Object>> validate(Object object) {
    return VALIDATOR.validate(object);
  }

  /**
   * Validates all constraints on the object and if not valid, throws a ConstraintViolationException
   * detailing the failure reasons if any.
   *
   * @param object the object to validate.
   * @throws ConstraintViolationException in case errors detected during the validation.
   */
  public static void ensureValid(Object object) {
    var violations = validate(object);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(buildViolationsMessage(violations), violations);
    }
  }

  private static String buildViolationsMessage(Set<ConstraintViolation<Object>> violations) {
    var buf = new StringBuilder();
    buf.append("Object has ").append(violations.size()).append(" validation errors. ");
    var n = 0;
    for (ConstraintViolation<Object> violation : violations) {
      if (++n > 1) {
        buf.append(", ");
      }
      buf.append("Violation ").append(n).append(": ");
      buf.append(violation.getPropertyPath())
          .append(" = ").append(violation.getInvalidValue()).append(" --> ")
          .append(violation.getMessage());
    }
    return buf.toString();
  }
}
