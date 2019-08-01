package com.teamcqr.chocolatequestrepoured.intrusive;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Abstracts away the use of intrusive modding methods such as ASM or Reflection API
 * At the moment this class has very limited functionality, all of which is based upon reflection. Much more is planned in this area.
 *
 * NOTE: In certain use cases, the Forge Access Transformer system can be a viable alternative to this utility.
 *
 * Use http://export.mcpbot.bspk.rs/stable/ for name references
 * MCP Name = development environment
 * Searge Name = compiled/release environment
 *
 * @author jdawg3636
 * @version 18/07/2019
 */
public class IntrusiveModificationHelper {

    /*
     * Boiler Plate - Please ignore
     */

    // Prevent instantiation (all methods are static)
    private IntrusiveModificationHelper() {}

    /*
     * Java Reflection API
     *
     * Uses more resources than usual, but shouldn't have any
     * inherent consequences in terms of compatibility or stability
     */

    /**
     * Returns requested Field with accessibility set to true
     * Accepts multiple potential names to handle variations between dev and release environments (MCP vs Searge naming)
     * @return requested Field or null
     */
    public static Field reflectGetField(Object instanceToAccess, String[] possibleNames) {

        // Return var
        Field toReturn;

        // Attempt access for each provided name; return if valid
        for(String name : possibleNames) {

            // Try/catch must be recreated each time as exceptions are expected and would otherwise terminate block
            try {

                toReturn = instanceToAccess.getClass().getField(name);
                toReturn.setAccessible(true);
                return toReturn;

            } catch (Exception ignored) {}

        }

        // Default (no provided names were found)
        return null;

    }

    /**
     * Returns name of provided field
     * @return name of provided field
     */
    public static String reflectGetFieldName(Field toGetNameOf) {

        return toGetNameOf.getName();

    }

    /**
     * Returns value of provided field
     * @return value of provided field
     */
    public static Object reflectGetFieldValue(Object instance, Field toGetValueOf) {

        // Attempt Field Access
        try {
            toGetValueOf.setAccessible(true); // Unnecessary if using solely this util; provided for compatibility
            return toGetValueOf.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Default - this means that something is wrong
        return null;

    }

    /**
     * Returns all fields of a provided instance and sets their accessibility status to true
     * @param instanceToAccess Instance to access
     * @return Field[]
     */
    public static Field[] reflectGetAllFields(Object instanceToAccess) {

        Field[] fieldsFromInstance = instanceToAccess.getClass().getDeclaredFields();

        if(instanceToAccess.getClass().getSuperclass() != null && instanceToAccess.getClass().getSuperclass() != Object.class) {

            // Recursive call
            Field[] fieldsFromSuperclass = reflectGetAllFields(instanceToAccess.getClass().getSuperclass());
            // Temp var
            Field[] existingFields = fieldsFromInstance;

            // Add temp var + recursive result to fieldsFromInstance
            fieldsFromInstance = new Field[existingFields.length + fieldsFromSuperclass.length];
            System.arraycopy(existingFields, 0, fieldsFromInstance, 0, existingFields.length);
            System.arraycopy(fieldsFromSuperclass, 0, fieldsFromInstance, existingFields.length, fieldsFromSuperclass.length);

        }

        try {

            for( Field f : fieldsFromInstance ) {
                f.setAccessible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fieldsFromInstance;

    }

    /**
     * Returns all field names of a provided instance
     * @param instanceToAccess instance to access
     * @return String[] names
     */
    public static String[] reflectGetAllFieldNames(Object instanceToAccess) {

        Field[] fieldsFromInstance = reflectGetAllFields(instanceToAccess);
        String[] toReturn = new String[fieldsFromInstance.length];

        for(int i = 0; i < toReturn.length; i++)
            toReturn[i] = reflectGetFieldName(fieldsFromInstance[i]);

        return toReturn;

    }

    /**
     * Returns all field values of a provided instance, regardless of accessibility status, as an Object[]
     * @param instanceToAccess Instance to access
     * @return Object[]
     */
    public static Object[] reflectGetAllFieldValues(Object instanceToAccess) {

        Field[] fieldsFromInstance = reflectGetAllFields(instanceToAccess);
        Object[] toReturn = new Object[fieldsFromInstance.length];

        try {

            for(int i = 0; i < toReturn.length; i++)
                toReturn[i] = reflectGetFieldValue(instanceToAccess, fieldsFromInstance[i]);

        } catch ( Exception e ) {
            e.printStackTrace();
        }


        return toReturn;

    }

    /**
     * Returns a list of all types used as a parameter in one or more constructors for the type of the provided instance
     * @param instanceToAccess instance to access
     * @param excludeOwnType whether to ignore constructor params of the object's own type; useful for filtering out "copier constructors"
     * @return Object[]
     */
    public static ArrayList<Class> reflectGetAllTypesUsedInConstructorParameters(Object instanceToAccess, boolean excludeOwnType) {

        ArrayList<Class> toReturn = new ArrayList<>();

        for(Constructor c : instanceToAccess.getClass().getConstructors())
            for(Class cl : c.getParameterTypes())
                if( !cl.equals(instanceToAccess.getClass()) || !excludeOwnType ) toReturn.add(cl);

        return toReturn;

    }

    /*
     * ASM
     *
     * Bypasses Forge and interfaces directly with FML to directly modify the vanilla game
     *
     * Extremely intrusive and highly discouraged by Forge developers. Can easily
     * cause stability/compatibility issues in addition to the inherent performance
     * penalties. Should be used only when no other options are available.
     */

    // Not yet implemented

}
