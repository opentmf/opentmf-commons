package org.opentmf.commons.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.opentmf.commons.validation.ValidationUtil.validate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentmf.commons.validation.constraints.SafeUrl;

/**
 * @author Gokhan Demir
 */
class SafeUrlValidationTests {

  @ParameterizedTest
  @ValueSource(strings = {
      "",
      "https://api.example.com/v1/customer/12345",
      "http://api.example.com/v1/customer/12345",
      "https://api.example.com:8080/v1/customer/12345",
      "https://api.example.com/v1/customer/12345?expand=characteristics",
      "https://api.example.com/v1/customer/12345#section",
      "https://api.example.com/path%20with%20encoded%20spaces",
      "https://xn--mller-kva.example.com/path",
      "/customer/12345",
      "/customer/12345?expand=foo",
      "customer/12345",
      "customer/12345?expand=foo&type=bar",
      "../sibling/123",
      "#fragment-only"
  })
  void testSafeUrl_acceptsValidValues(String value) {
    assertThat(validate(new Sample(value))).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "javascript:alert(1)",
      "data:text/plain,hello",
      "file:///etc/passwd",
      "ftp://server/path",
      "mailto:foo@bar.com",
      "https://",
      "https:///path-without-host",
      "https://example.com/path with unencoded space",
      "https://example.com/<script>",
      "https://example.com/back\\slash",
      "https://example.com/\rinjected",
      "https://example.com/\ninjected",
      "https://example.com/\tinjected"
  })
  void testSafeUrl_rejectsDangerousOrMalformedValues(String value) {
    assertThat(validate(new Sample(value))).hasSize(1);
  }

  @RequiredArgsConstructor
  @Getter
  private static class Sample {

    @SafeUrl
    private final String href;
  }
}
