package org.opentmf.commons.validation.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.opentmf.commons.validation.RequiredValidator;

/**
 * Class level NotNull annotation for multiple fields.
 *
 * <p>Validates only and only if, at the time of the validation, the initialized @Required
 * belongs to the actual declaring class itself, not to a parent class.
 * </p>
 *
 * @author Gokhan Demir
 */
@Documented
@Constraint(validatedBy = {RequiredValidator.class})
@Target({TYPE})
@Retention(RUNTIME)
public @interface Required {

  /** @return the error message template. */
  String message() default "Missing values for the required {nullFields}";

  /** @return the validation groups. */
  Class<?>[] groups() default {};

  /** @return the payload. */
  Class<? extends Payload>[] payload() default {};

  /**
   * @return The list of the field names that are required (i.e. not null).
   */
  String[] fields();
}
