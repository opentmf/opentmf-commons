package org.opentmf.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.opentmf.commons.util.ListUtil.safe;
import static org.opentmf.commons.util.ListUtil.safeMutable;

import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Gokhan Demir
 */
class ListUtilTests {

  @Test
  void testSafe_withNullList_returnsImmutableList() {
    var safeList = safe(null);
    assertEquals(Collections.emptyList(), safeList);
    Assertions.assertThrows(UnsupportedOperationException.class, () -> safeList.add("x"));
  }

  @Test
  void testSafe_withExistingList_returnsImmutableList() {
    var safeList = safe(new ArrayList<String>());
    Assertions.assertThrows(UnsupportedOperationException.class, () -> safeList.add("x"));
  }

  @Test
  void testSafeMutable_withNullList_returnsMutableList() {
    var safeList = safeMutable(null);
    assertEquals(Collections.emptyList(), safeList);
    Assertions.assertDoesNotThrow(() -> safeList.add("x"));
  }

  @Test
  void testSafeMutable_withExistingMutableList_returnsMutableList() {
    var safeList = safeMutable(new ArrayList<String>());
    Assertions.assertDoesNotThrow(() -> safeList.add("x"));
  }

  @Test
  void testSafeMutable_withExistingImmutableList_returnsMutableList() {
    var existingImmutableList = safe(null);
    var safeMutableList = safeMutable(existingImmutableList);
    Assertions.assertDoesNotThrow(() -> safeMutableList.add("x"));
  }
}
