package org.opentmf.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.opentmf.commons.util.PropertyUtil.propertyToEnvVariable;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Gokhan Demir
 */
class PropertyUtilTests {

  static Stream<Arguments> validConversions() {
    return Stream.of(
        Arguments.of("ADD_NUMBERS_FAILED", "add.numbers.failed"),
        Arguments.of("ADD_NUMBERS_FAILED", "addNumbersFailed"),
        Arguments.of("ADD_NUMBERS_FAILED", "AddNumbersFailed"),
        Arguments.of("ADD_NUMBERS_FAILED", "ADD.NumbersFailed"),
        Arguments.of("MAIN_NUMBER_VALIDATION_FAILED", "MainNumberValidationFailed"),
        Arguments.of("SERVICE_ORDER_CREATED", "ServiceOrderCreated"),
        Arguments.of("SERVICE_ORDER_COMPLETED", "ServiceOrderCompleted"));
  }

  @ParameterizedTest
  @MethodSource("validConversions")
  void testPropertyToEnvVariable_withAllPossibleCombinations_returnsValidResult(
      String envVar, String property) {
    assertEquals(envVar, propertyToEnvVariable(property));
  }
}
