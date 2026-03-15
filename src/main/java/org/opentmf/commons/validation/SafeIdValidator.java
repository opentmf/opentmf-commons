package org.opentmf.commons.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.opentmf.commons.validation.constraints.SafeId;

/**
 * @author Gokhan Demir
 */
public class SafeIdValidator implements ConstraintValidator<SafeId, CharSequence> {

  @Override
  public boolean isValid(CharSequence text, ConstraintValidatorContext context) {
    if (text == null) {
      return true;
    }
    return RegexValidations.SAFE_ID.matches(text.toString());
  }
}
