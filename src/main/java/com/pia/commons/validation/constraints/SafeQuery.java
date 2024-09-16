package com.pia.commons.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.pia.commons.validation.SafeQueryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows only certain safe characters to exist in a query string.
 * <p>
 *   The allowed characters are:
 *   <ul>
 *     <li>Alphanumeric characters</li>
 *     <li>Equals (=)</li>
 *     <li>Minus (-)</li>
 *     <li>Plus (+)</li>
 *     <li>Space ( )</li>
 *     <li>Asterisk (*)</li>
 *     <li>Dot (.)</li>
 *     <li>Underscore (_)</li>
 *     <li>Ampersand (&)</li>
 *   </ul>
 * </p>
 *
 * @author Gokhan Demir
 */
@Documented
@Constraint(validatedBy = {SafeQueryValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface SafeQuery {

  String message() default "Only alphanumeric characters, equals, minus, plus, space, ampersand, "
      + "asterisk, dot, underscore and ampersand are allowed.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
