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
import jakarta.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Validates that the annotated value is a syntactically valid URL or relative URI reference.
 *
 * <p>This is a meta-composed synonym for {@link SafeUrl}: the two annotations apply the
 * same {@link org.opentmf.commons.validation.SafeUrlValidator} and accept exactly the same
 * inputs. Existing call sites continue to work without behavior change.
 *
 * @author Gokhan Demir
 * @deprecated since 2.2.0, for removal in a future major release. Use {@link SafeUrl}
 *     instead. The {@code @SafeQuery} name predates the realization that the constraint is
 *     about URL/href values rather than filter-query expressions; {@code @SafeUrl} states
 *     the intent plainly. Migration is a search-and-replace.
 */
@Documented
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@ReportAsSingleViolation
@SafeUrl
@Deprecated(since = "2.2.0", forRemoval = true)
public @interface SafeQuery {

  /** @return the error message template. */
  String message() default "Must be a valid URL or relative URI reference using http/https.";

  /** @return the validation groups. */
  Class<?>[] groups() default {};

  /** @return the payload. */
  Class<? extends Payload>[] payload() default {};
}
