package com.pia.commons.validation;

import com.pia.commons.validation.constraints.SafeJsonPath;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Gokhan Demir
 */
public class SafeJsonPathValidator implements ConstraintValidator<SafeJsonPath, CharSequence> {

  @Override
  public boolean isValid(CharSequence charSequence, ConstraintValidatorContext context) {
    if (charSequence == null) {
      return true;
    }
    return RegexValidations.SAFE_JSON_PATH.matches(charSequence.toString());
  }
}
