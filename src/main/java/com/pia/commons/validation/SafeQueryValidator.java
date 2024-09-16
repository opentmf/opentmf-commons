package com.pia.commons.validation;

import com.pia.commons.validation.constraints.SafeQuery;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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
