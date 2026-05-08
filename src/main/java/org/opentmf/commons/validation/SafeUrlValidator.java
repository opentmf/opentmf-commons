package org.opentmf.commons.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Set;
import org.opentmf.commons.validation.constraints.SafeUrl;

/**
 * @author Gokhan Demir
 */
public class SafeUrlValidator implements ConstraintValidator<SafeUrl, CharSequence> {

  private static final Set<String> ALLOWED_SCHEMES = Set.of("http", "https");

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    String s = value.toString();
    if (s.isEmpty()) {
      return true;
    }
    if (containsControlCharacters(s)) {
      return false;
    }
    try {
      URI uri = new URI(s);
      String scheme = uri.getScheme();
      if (scheme == null) {
        return true;
      }
      if (!ALLOWED_SCHEMES.contains(scheme.toLowerCase(Locale.ROOT))) {
        return false;
      }
      return uri.getHost() != null && !uri.getHost().isEmpty();
    } catch (URISyntaxException e) {
      return false;
    }
  }

  private static boolean containsControlCharacters(String s) {
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c < 0x20 || c == 0x7F) {
        return true;
      }
    }
    return false;
  }
}
