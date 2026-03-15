package org.opentmf.commons.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.opentmf.commons.validation.SafeIdValidator;

/**
 * Allows only certain safe characters within the text field to defend against potential code
 * injection attacks.
 * The allowed characters are:
 * <ul>
 *   <li>Alphanumeric characters</li>
 *   <li>Minus (-)</li>
 *   <li>Underscore (_)</li>
 * </ul>
 *
 * @author Gokhan Demir
 */
@Documented
@Constraint(validatedBy = {SafeIdValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface SafeId {

  /** @return the error message template. */
  String message() default "Only alphanumeric characters, minus, and underscore are allowed.";

  /** @return the validation groups. */
  Class<?>[] groups() default {};

  /** @return the payload. */
  Class<? extends Payload>[] payload() default {};
}
