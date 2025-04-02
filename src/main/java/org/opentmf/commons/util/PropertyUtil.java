package org.opentmf.commons.util;

import lombok.Generated;

/**
 * A helper for Spring properties.
 *
 * @author Gokhan Demir
 */
public final class PropertyUtil {

  @Generated
  private PropertyUtil() {
    throw new UnsupportedOperationException(
        "PropertyUtil is a utility class with only "
            + "static methods, therefore cannot be instantiated.");
  }

  /**
   * Converts the given property to its counterpart environment variable that is recognized by the
   * Spring Boot externalized configuration.
   *
   * @param property The property to be converted to its counterpart environment variable.
   * @return The environment variable name that will override the value of this property if
   *     specified.
   * @see <a
   *     href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.external-config">
   *     Spring Boot Externalized Configuration</a>
   */
  public static String propertyToEnvVariable(String property) {
    var buf = new StringBuilder();
    for (int i = 0, n = property.length(); i < n; i++) {
      var c = property.charAt(i);
      if (Character.isUpperCase(c) && i > 0 && Character.isLowerCase(property.charAt(i - 1))) {
        buf.append('_').append(c);
      } else if (c == '.') {
        buf.append('_');
      } else {
        buf.append(Character.toUpperCase(c));
      }
    }
    return buf.toString();
  }
}
