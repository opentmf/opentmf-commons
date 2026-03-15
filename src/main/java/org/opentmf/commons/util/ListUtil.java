package org.opentmf.commons.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Generated;

/**
 * Contains helper methods to manipulate lists.
 *
 * @author Cezmi Aslan
 * @author Gokhan Demir
 */
public final class ListUtil {

  @Generated
  private ListUtil() {
    throw new UnsupportedOperationException("ListUtil is a utility class only with static methods, "
        + "hence cannot be instantiated.");
  }

  /**
   * Creates a safe (immutable) version of the given list.
   * If the list is null, returns an empty immutable list.
   *
   * @param list the list to make safe
   * @param <T> the element type
   * @return a safe (immutable) version of the given list.
   */
  public static <T> List<T> safe(List<T> list) {
    if (list == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(list);
  }

  /**
   * Returns a safe (mutable) version of the given list.
   * If the list is null, returns a new empty ArrayList.
   *
   * @param list the list to make safe
   * @param <T> the element type
   * @return a safe (mutable) version of the given list
   */
  public static <T> List<T> safeMutable(List<T> list) {
    if (list == null) {
      return new ArrayList<>();
    }
    return new ArrayList<>(list);
  }
}
