package org.opentmf.commons.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.opentmf.commons.validation.constraints.SafeText;

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
