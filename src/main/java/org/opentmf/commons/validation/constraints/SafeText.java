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
import org.opentmf.commons.validation.SafeTextValidator;

/**
 * Allows only certain safe characters within the text field to defend against potential code
 * injection attacks. Unicode letters, marks (combining accents), and digits are allowed so that
 * names like {@code François} or {@code O'Brien} validate successfully.
 *
 * <p>This constraint is a defense-in-depth measure. It is not a substitute for parameterized
 * queries (against SQL injection) or contextual output encoding (against XSS), both of which must
 * still be applied at the boundaries that consume the validated value.
 *
 * <p>The allowed characters are:
 * <ul>
 *   <li>Unicode letters ({@code \p{L}}) and combining marks ({@code \p{M}})</li>
 *   <li>Unicode digits ({@code \p{N}})</li>
 *   <li>Space ( )</li>
 *   <li>Underscore (_)</li>
 *   <li>Apostrophe (') and right single quotation mark (’)</li>
 *   <li>Minus (-), Plus (+)</li>
 *   <li>Percent (%), Asterisk (*)</li>
 *   <li>Dot (.), Comma (,), Colon (:)</li>
 *   <li>Slash (/)</li>
 *   <li>Question mark (?), Exclamation mark (!)</li>
 *   <li>Parentheses ( and )</li>
 * </ul>
 *
 * <p>Characters such as {@code < > " ` ; = { } [ ] \ | $ # ^ ~ @} and control characters are
 * rejected because they are common building blocks of SQL or script injection payloads.
 *
 * @author Yusuf Bozkurt
 */
@Documented
@Constraint(validatedBy = {SafeTextValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface SafeText {

  /** @return the error message template. */
  String message() default "Only letters, digits, spaces, and the characters "
        + "_ - + % * . , : / ? ! ( ) and apostrophes are allowed.";

  /** @return the validation groups. */
  Class<?>[] groups() default {};

  /** @return the payload. */
  Class<? extends Payload>[] payload() default {};
}
