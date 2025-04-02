package org.opentmf.commons.util.fieldselection;

import java.beans.PropertyDescriptor;

public interface FieldHelper {
    boolean isEmbeddedId(Class<?> clazz, PropertyDescriptor pd);
}
