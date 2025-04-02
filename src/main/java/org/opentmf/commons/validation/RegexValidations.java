package org.opentmf.commons.validation;

import java.util.regex.Pattern;

/**
 * @author Yusuf Bozkurt
 */
enum RegexValidations {

  SAFE_ID(Pattern.compile("^[\\w-]*$")),
  SAFE_TEXT(Pattern.compile("^[\\w-+%*.:/ ]*$")),
  SAFE_JSON_PATH(Pattern.compile("^[\\w-+%$'~\\[\\](|):,?<>=&!@*./ ]*$")),
  SAFE_QUERY(Pattern.compile("^[\\w-+@*=&. ]*$"));

  private final Pattern pattern;

  RegexValidations(Pattern pattern) {
    this.pattern = pattern;
  }

  public boolean matches(String text) {
    return this.pattern.matcher(text).matches();
  }
}