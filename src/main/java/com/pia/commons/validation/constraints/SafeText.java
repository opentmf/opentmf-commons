package com.pia.commons.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.pia.commons.validation.SafeTextValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Allows only certain safe characters within the text field to defend against potential code
 * injection attacks.
 * <p>
 *   The allowed characters are:
 *   <ul>
 *     <li>Alphanumeric characters</li>
 *     <li>Minus (-)</li>
 *     <li>Plus (+)</li>
 *     <li>Space ( )</li>
 *     <li>Asterisk (*)</li>
 *     <li>Slash (/)</li>
 *     <li>Dot (.)</li>
 *     <li>Colon (:)</li>
 *     <li>Underscore (_)</li>
 *   </ul>
 * </p>
 *
 * @author Yusuf Bozkurt
 */
@Documented
@Constraint(validatedBy = {SafeTextValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface SafeText {

  String message() default "Only alphanumeric characters, minus, plus, space, asterisk, "
        + "slash, dot, colon and underscore are allowed.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
