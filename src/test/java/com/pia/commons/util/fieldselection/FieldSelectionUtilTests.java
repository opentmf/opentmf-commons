package com.pia.commons.util.fieldselection;

import java.util.Map;

import com.pia.commons.util.repository.entity.Professor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.pia.commons.util.fieldselection.FieldSelectionUtil.parseFields;
import static com.pia.commons.util.fieldselection.FieldSelectionUtil.resolveProperties;

/**
 * @author Abdullah Beker
 */
class FieldSelectionUtilTests {

  @Test
  void testResolveProperties_withDepthLevelOne_resolvesRootLevelFieldsAndExpandsOneLevel() {
    Map<String, FieldSelectionUtil.FieldNode> map = resolveProperties(Professor.class, 1);
    Assertions.assertNotNull(map);
  }

  @Test
  void testParseFields_withEmbeddedId_returnEmbeddedIdFieldValue() {
    Map<String, FieldSelectionUtil.FieldNode> map = parseFields(Professor.class, "name,classroom");
    Assertions.assertNotNull(map);
  }
}
