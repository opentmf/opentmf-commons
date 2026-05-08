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
 * Allows only ASCII-safe identifier characters to defend against potential code injection
 * attacks. Intended for opaque identifiers such as UUIDs, slugs, and sequence numbers, where
 * accented or non-Latin characters are not expected. For human-readable text use
 * {@link SafeText} instead.
 *
 * <p>The allowed characters are:
 * <ul>
 *   <li>ASCII letters (A-Z, a-z) and digits (0-9)</li>
 *   <li>Underscore (_)</li>
 *   <li>Minus (-)</li>
 * </ul>
 *
 * <p>The pattern accepts the empty string. To require a non-empty value, combine with
 * {@code @Size(min = 1)} or {@code @NotBlank}.
 *
 * <p>This constraint is a defense-in-depth measure. Parameterized queries (against SQL
 * injection) and contextual output encoding (against XSS) must still be applied at the
 * boundaries that consume the validated value.
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
