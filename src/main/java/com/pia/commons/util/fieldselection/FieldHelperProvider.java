package com.pia.commons.util.fieldselection;

public class FieldHelperProvider {

    private static FieldHelper fieldHelper;

    static {
        try {
            Class.forName("jakarta.persistence.Entity", false, FieldHelperProvider.class.getClassLoader());
            fieldHelper = new PersistentFieldHelper();
        } catch (ClassNotFoundException e) {
            fieldHelper = new DefaultFieldHelper();
        }
    }

    public static FieldHelper getFieldHelper() {
        return fieldHelper;
    }
}
