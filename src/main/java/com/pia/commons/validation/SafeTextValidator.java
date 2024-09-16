package com.pia.commons.validation;

import com.pia.commons.validation.constraints.SafeText;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author Yusuf Bozkurt
 */
public class SafeTextValidator implements ConstraintValidator<SafeText, Object> {

  @Override
  public boolean isValid(Object text, ConstraintValidatorContext context) {
    if (Objects.nonNull(text) && text instanceof CharSequence s) {
      return RegexValidations.SAFE_TEXT.matches(s.toString());
    }
    return true;
  }
}
