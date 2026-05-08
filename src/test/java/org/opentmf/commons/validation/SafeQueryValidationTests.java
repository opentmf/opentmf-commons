package org.opentmf.commons.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.opentmf.commons.validation.ValidationUtil.validate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentmf.commons.validation.constraints.SafeQuery;

/**
 * Confirms that the deprecated {@code @SafeQuery} annotation continues to behave as a
 * meta-composed synonym for {@code @SafeUrl}.
 *
 * @author Gokhan Demir
 */
@SuppressWarnings("deprecation")
class SafeQueryValidationTests {

  @ParameterizedTest
  @ValueSource(strings = {
      "",
      "https://api.example.com/v1/customer/12345",
      "/customer/12345",
      "customer/12345?expand=foo"
  })
  void testSafeQuery_acceptsValidValues(String value) {
    assertThat(validate(new Sample(value))).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "javascript:alert(1)",
      "data:text/plain,hello",
      "https://example.com/<script>",
      "https://example.com/\rinjected"
  })
  void testSafeQuery_rejectsDangerousValues(String value) {
    assertThat(validate(new Sample(value))).hasSize(1);
  }

  @RequiredArgsConstructor
  @Getter
  private static class Sample {

    @SafeQuery
    private final String href;
  }
}
