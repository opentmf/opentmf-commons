package com.pia.commons.validation;

import com.pia.commons.validation.constraints.SafeId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author Gokhan Demir
 */
public class SafeIdValidator implements ConstraintValidator<SafeId, CharSequence> {

  @Override
  public boolean isValid(CharSequence text, ConstraintValidatorContext context) {
    if (Objects.nonNull(text)) {
      return RegexValidations.SAFE_ID.matches(text.toString());
    }
    return true;
  }
}
