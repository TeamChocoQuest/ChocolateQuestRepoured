package com.teamcqr.chocolatequestrepoured.util.data;

public class PrimitiveManipulationUtil {

    // Prevent instantiation
    private PrimitiveManipulationUtil() {}

    // Check if primitive
    public static boolean isPrimitive(Object o) {
        if(o instanceof Boolean) return true;
        if(o instanceof Character) return true;
        if(o instanceof Byte) return true;
        if(o instanceof Short) return true;
        if(o instanceof Integer) return true;
        if(o instanceof Long) return true;
        if(o instanceof Float) return true;
        if(o instanceof Double) return true;
        if(o instanceof Void) return true;
        return false;
    }

}
