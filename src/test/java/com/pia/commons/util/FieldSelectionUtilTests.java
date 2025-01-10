package com.pia.commons.util;

import java.util.Map;

import com.pia.commons.util.repository.entity.Professor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.pia.commons.util.FieldSelectionUtil.resolveProperties;

/**
 * @author Abdullah Beker
 */
class FieldSelectionUtilTests {

  @Test
  void testResolveProperties_withDepthLevelOne_resolvesRootLevelFieldsAndExpandsOneLevel() {
    Map<String, FieldSelectionUtil.FieldNode> map = resolveProperties(Professor.class, 1);
    Assertions.assertNotNull(map);
  }
}
