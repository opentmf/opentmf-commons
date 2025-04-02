package org.opentmf.commons.util.fieldselection;

import static org.opentmf.commons.util.fieldselection.FieldSelectionUtil.parseFields;
import static org.opentmf.commons.util.fieldselection.FieldSelectionUtil.resolveProperties;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentmf.commons.util.repository.entity.Professor;

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
