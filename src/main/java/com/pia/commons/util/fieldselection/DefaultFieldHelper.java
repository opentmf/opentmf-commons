package com.pia.commons.util.fieldselection;

import java.beans.PropertyDescriptor;

public class DefaultFieldHelper implements FieldHelper {
  @Override
  public boolean isEmbeddedId(Class<?> clazz, PropertyDescriptor pd) {
    return false;
  }
}
