package org.opentmf.commons.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.opentmf.commons.validation.constraints.SafeQuery;

/**
 * @author Gokhan Demir
 */
public class SafeQueryValidator implements ConstraintValidator<SafeQuery, CharSequence> {

  @Override
  public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
    if (charSequence == null) {
      return true;
    }
    return RegexValidations.SAFE_QUERY.matches(charSequence.toString());
  }
}
