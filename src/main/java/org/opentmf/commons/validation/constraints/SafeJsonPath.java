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
import org.opentmf.commons.validation.SafeJsonPathValidator;

/**
 * Allows only certain safe characters to exist in a JsonPath string such as
 * {@code $.store.book[?(@.price > $.expensive)]} or {@code $..author}. Unicode letters, marks,
 * and digits are accepted so that paths reference non-Latin keys (for example,
 * {@code $.müşteri.ad}).
 *
 * <p>The allowed characters are:
 * <ul>
 *   <li>Unicode letters ({@code \p{L}}), combining marks ({@code \p{M}}), and digits
 *       ({@code \p{N}})</li>
 *   <li>Space ( ) and Underscore (_)</li>
 *   <li>JsonPath structure: {@code $ . [ ] ( ) | : , ? * @ ~ /}</li>
 *   <li>JsonPath operators: {@code = != < > & ! +} and Apostrophe (') for string literals</li>
 *   <li>Minus (-), Percent (%)</li>
 * </ul>
 *
 * <p>Characters such as {@code " ` { } \ ; # ^} and control characters are rejected.
 *
 * <p>This constraint is a defense-in-depth measure and assumes the validated value is parsed
 * by a real JsonPath engine — never concatenated into SQL or rendered into HTML without
 * contextual encoding.
 *
 * @author Gokhan Demir
 */
@Documented
@Constraint(validatedBy = {SafeJsonPathValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface SafeJsonPath {

  /** @return the error message template. */
  String message() default "Only letters, digits, spaces, and JsonPath special characters "
      + "are allowed.";

  /** @return the validation groups. */
  Class<?>[] groups() default {};

  /** @return the payload. */
  Class<? extends Payload>[] payload() default {};
}
