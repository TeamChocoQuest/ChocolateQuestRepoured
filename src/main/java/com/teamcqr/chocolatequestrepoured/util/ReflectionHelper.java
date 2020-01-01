package com.teamcqr.chocolatequestrepoured.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.teamcqr.chocolatequestrepoured.util.data.ArrayCollectionMapManipulationUtil;

/**
 * Abstracts away the use of Java's Reflection API
 *
 * NOTE: In many cases, the Forge Access Transformer system is a viable alternative to this utility.
 *
 * Use http://export.mcpbot.bspk.rs/stable/ for name references
 * MCP Name = development environment
 * Searge Name = compiled/release environment
 *
 * @author jdawg3636
 * @version 1 October 2019
 */
public class ReflectionHelper {

    // Prevents instantiation (all methods are static)
    private ReflectionHelper() {}

    /*
     * Class Objects
     */

    /**
     * Returns an instance of thee provided class
     * @return generated instance, or null if unsuccessful
     */
    public static Object getInstanceOfClass(Class c) {
        try {
            return c.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a Class object when given its verbose name as a String
     * @return requested Class, or null if unsuccessful
     */
    public static Class getClassFromName(String verboseTypeNameAsSting) {
        try {
            return Class.forName(verboseTypeNameAsSting);
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * Fields
     */

    /**
     * Returns requested Field with accessibility set to true
     * Accepts multiple potential names to handle variations between dev and release environments (MCP vs Searge naming)
     *
     * NOTE: This method intentionally uses the reflectGetAllFields utility method from this class, causing a minor
     * performance loss when compared directly to the getField method of a class object. This is done to avoid
     * recreating the recursive structure necessary to get all fields from superclasses using getDeclaredField(s)
     * (the getField(s) methods don't return private fields, which defeats the entire purpose of this util). If being
     * used in a situation where the performance penalty from this actually matters, this util should probably not be
     * being used anyway.
     *
     * @return requested Field or null
     */
    public static Field reflectGetField(Object instanceToAccess, String[] possibleNames) {

        // Get all fields
        Field[] allFields = reflectGetAllFields(instanceToAccess);

        // Check each entry in both arrays and return if equal
        for(Field field : allFields) {
            for(String name : possibleNames) {
                if(reflectGetFieldName(field).equals(name)) {
                    return field;
                }
            }
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
        }
        // Thrown if value of field == null
        catch (NullPointerException npe) {
            return null;
        }
        // Catch-all exception for debug purposes - if this is called then something is wrong
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Sets the value of the given field to the given value for the given instance
     */
    public static Object reflectSetFieldValue(Object instance, Field toSet, Object value) {

        // Ensure accessible
        toSet.setAccessible(true);
        // Set
        try {
            toSet.set(instance, value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return modified instance to allow chaining
        return instance;

    }

    /**
     * Returns all fields of a provided Class object or instance thereof and sets their accessibility status to true
     * USES RECURSION TO RETRIEVE SUPERCLASS FIELDS - USE SPARINGLY
     * @return Field[]
     */
    public static Field[] reflectGetAllFields(Object instanceOrClassToAccess) {

        // Variables
        Class classToAccess;
        Field[] fieldsFromClass = new Field[] {};
        Field[] fieldsFromSuperclass = new Field[] {};

        // Convert parameter to instance of Class
        if(instanceOrClassToAccess instanceof Class) {
            classToAccess = (Class) instanceOrClassToAccess;
        } else {
            classToAccess = instanceOrClassToAccess.getClass();
        }

        // Retrieve fields
        fieldsFromClass = classToAccess.getDeclaredFields();

        if(classToAccess.getSuperclass() != null && classToAccess.getSuperclass() != Object.class) {

            // Recursive call
            try {
                fieldsFromSuperclass = reflectGetAllFields( classToAccess.getSuperclass() );
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Combine Arrays
            fieldsFromClass = (Field[])ArrayCollectionMapManipulationUtil.combineArrays(fieldsFromClass, fieldsFromSuperclass);

        }

        // Remove accessibility limitations
        try {
            for( Field f : fieldsFromClass ) f.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return
        return fieldsFromClass;

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
     * Methods
     */

    /**
     * Sets all methods of the provided instance to public
     * @return Copy of the modified instance (facilitates chaining)
     */
    public static Object reflectGetInstanceWithAllMethodsPublic(Object instanceToAccess) {

        Method[] methods = reflectGetAllMethods(instanceToAccess);
        for(Method  m : methods) m.setAccessible(true);

        // Return modified instance to allow chaining
        return instanceToAccess;

    }

    /**
     * Returns Reflect API Method objects to represent each method present in the instance/class provided.
     * Accepts both normal instances and Class object representations
     * @return Method[]
     */
    public static Method[] reflectGetAllMethods(Object instanceOrClassToAccess) {

        // Variables
        Class classToAccess;
        Method[] methodsFromClass = new Method[] {};
        Method[] methodsFromSuperclass = new Method[] {};

        // Convert parameter to instance of Class
        if(instanceOrClassToAccess instanceof Class) {
            classToAccess = (Class) instanceOrClassToAccess;
        } else {
            classToAccess = instanceOrClassToAccess.getClass();
        }

        // Retrieve methods
        methodsFromClass = classToAccess.getDeclaredMethods();

        if(classToAccess.getSuperclass() != null && classToAccess.getSuperclass() != Object.class) {

            // Recursive call
            try {
                methodsFromSuperclass = reflectGetAllMethods( classToAccess.getSuperclass() );
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Combine Arrays
            methodsFromClass = (Method[])ArrayCollectionMapManipulationUtil.combineArrays(methodsFromClass, methodsFromSuperclass);

        }

        // Remove accessibility limitations
        try {
            for( Method m : methodsFromClass ) m.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return
        return methodsFromClass;
    }

}
