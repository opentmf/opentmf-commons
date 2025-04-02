package org.opentmf.commons.util;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.opentmf.commons.util.fieldselection.DefaultFieldHelper;
import org.opentmf.commons.util.fieldselection.FieldHelperProvider;

/**
 * @author Gokhan Demir
 */
class FieldSelectionUtilTests {

  @Test
  void testFieldHelperProvider_returnsPersistentFieldHelper() {
    assertFalse(FieldHelperProvider.getFieldHelper() instanceof DefaultFieldHelper);
    assertFalse(new DefaultFieldHelper().isEmbeddedId(null, null));
  }
}
