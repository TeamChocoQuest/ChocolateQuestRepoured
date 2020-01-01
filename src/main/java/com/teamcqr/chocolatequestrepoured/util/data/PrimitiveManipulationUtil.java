package com.teamcqr.chocolatequestrepoured.util.data;

import com.teamcqr.chocolatequestrepoured.util.ReflectionHelper;

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
        return false;
    }

    // Convert a primitive type and value, represented as strings, to an instance of said prim with said value
    public static Object getPrimFromString(String verboseTypeNameAsSting, String valueAsString) {

        // Generate dummy object of provided type
        Object instanceOfType = null;
        try{
            instanceOfType = ReflectionHelper.getInstanceOfClass( ReflectionHelper.getClassFromName(verboseTypeNameAsSting) );
        }
        // If unable to instantiate, return null
        catch (Exception ignored) {
            return null;
        }

        // Check each primitive type and return appropriate value if match
        if(instanceOfType instanceof Boolean) {
            return Boolean.parseBoolean(valueAsString);
        }
        if(instanceOfType instanceof Character) {
            return valueAsString.charAt(0);
        }
        if(instanceOfType instanceof Byte) {
            return Byte.parseByte(valueAsString);
        }
        if(instanceOfType instanceof Short) {
            return Short.parseShort(valueAsString);
        }
        if(instanceOfType instanceof Integer) {
            return Integer.parseInt(valueAsString);
        }
        if(instanceOfType instanceof Long) {
            return Long.parseLong(valueAsString);
        }
        if(instanceOfType instanceof Float) {
            return Float.parseFloat(valueAsString);
        }
        if(instanceOfType instanceof Double) {
            return Double.parseDouble(valueAsString);
        }

        // If value does not match any primitive checks, return null
        return null;

    }

}
