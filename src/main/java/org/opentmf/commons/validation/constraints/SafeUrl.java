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
import org.opentmf.commons.validation.SafeUrlValidator;

/**
 * Validates that the annotated value is a syntactically valid URL or relative URI reference.
 * Suitable for TMF {@code href}, callback, and link fields, which routinely hold both
 * absolute URLs (e.g. {@code https://api.example.com/v1/customer/123}) and scheme-less
 * relative references (e.g. {@code /customer/123} or {@code customer/123?expand=foo}).
 *
 * <p>Validation is performed by parsing the value with {@link java.net.URI}, not by regex
 * character-class matching. The following rules apply:
 * <ul>
 *   <li>Absolute URLs must use {@code http} or {@code https}. Schemes such as
 *       {@code javascript}, {@code data}, {@code file}, {@code ftp}, and {@code mailto}
 *       are rejected.</li>
 *   <li>Absolute URLs must have a non-empty host component.</li>
 *   <li>Relative references (no scheme) are accepted.</li>
 *   <li>Strings containing ASCII control characters (including CR/LF, used for header
 *       injection) are rejected.</li>
 *   <li>{@code null} and the empty string are accepted. Combine with {@code @NotNull},
 *       {@code @Size(min = 1)}, or {@code @NotBlank} to require presence.</li>
 * </ul>
 *
 * <p>For stricter absolute-only validation (require https, pin a specific host, …) prefer
 * {@code @org.hibernate.validator.constraints.URL} from Hibernate Validator — note however
 * that {@code @URL} does <em>not</em> accept relative references, so it cannot replace
 * {@code @SafeUrl} on fields that may hold either form.
 *
 * <p>This constraint is a defense-in-depth measure. Open-redirect prevention (allowlist of
 * acceptable destinations) and contextual output encoding (when the value is rendered into
 * HTML attributes or HTTP {@code Location} headers) must still be applied at the
 * boundaries that consume the value.
 *
 * @author Gokhan Demir
 */
@Documented
@Constraint(validatedBy = {SafeUrlValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface SafeUrl {

  /** @return the error message template. */
  String message() default "Must be a valid URL or relative URI reference using http/https.";

  /** @return the validation groups. */
  Class<?>[] groups() default {};

  /** @return the payload. */
  Class<? extends Payload>[] payload() default {};
}
