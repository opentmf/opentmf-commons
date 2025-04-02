package org.opentmf.commons.util.fieldselection;

import lombok.Getter;
import lombok.experimental.UtilityClass;

/**
 * @author Abdullah Beker
 */
@UtilityClass
public class FieldHelperProvider {

    @Getter
    private static FieldHelper fieldHelper;

    static {
        try {
            Class.forName("jakarta.persistence.Entity", false, FieldHelperProvider.class.getClassLoader());
            fieldHelper = new PersistentFieldHelper();
        } catch (ClassNotFoundException e) {
            fieldHelper = new DefaultFieldHelper();
        }
    }
}
