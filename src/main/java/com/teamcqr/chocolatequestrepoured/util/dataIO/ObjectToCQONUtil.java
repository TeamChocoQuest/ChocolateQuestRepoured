package com.teamcqr.chocolatequestrepoured.util.dataIO;

import com.teamcqr.chocolatequestrepoured.intrusive.IntrusiveModificationHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Takes any object and converts all fields to/from CQON (Chocolate Quest Object Notation),
 * a proprietary format for representing a Java object in plain text for the purpose of serialization.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * * CQON Specification:                                                                                   *
 * * The tab character symbolizes possession (fields contained within other fields)                        *
 * * The space character indicates that the rest of the line is a primitive value                          *
 * * The new line character resets tab/space status                                                        *
 * *                                                                                                       *
 * * A new line character on a line that does not contain a space indicates that the relevant              *
 * * field is not primitive, and that the field's internal data will carry over to the following lines.    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Note that this class relies heavily on reflection, recursion,
 * and other suboptimal methods. If an object DOES implement
 * Serializable, a more efficient solution would be to send its
 * serialized value directly to FileIOUtil.
 *
 * @author jdawg3636
 * GitHub: https://github.com/jdawg3636
 *
 * @version 25.07.19
 */
public class ObjectToCQONUtil {

    /*
     * Util
     */

    // Must be manually applied - provided for programmer convenience
    public static HashMap<Class, String[]> getDefaultRelevancyLUT() {

        HashMap<Class, String[]> toReturn = new HashMap<>();

        toReturn.put(BlockPos.class, new String[]{"x", "y", "z"});

        return toReturn;

    }

    /*
     * Object --> CQON
     */

    public static ArrayList<Byte> convertObjectToCQON(Object toSerialize, HashMap<Class, String[]> relevantFieldLUT, int maxRecursionDepth) {

        ArrayList<Byte> toReturn = new ArrayList<>();

        // Use reflection in a recursive method to explore all nested fields until all values reduced to prims or until maxRecursionDepth reached
        for( Field field : IntrusiveModificationHelper.reflectGetAllFields(toSerialize)) {
            toReturn.addAll( recursiveConvertFieldToCQON(toSerialize, field, relevantFieldLUT,0, maxRecursionDepth) );
        }

        return toReturn;

    }

    // Recursive section of convertObjectToCQON
    // Uses reflection to explore all nested fields until remaining fields are all prims or until maxRecursionDepth is reached
    private static ArrayList<Byte> recursiveConvertFieldToCQON(Object origin, Field toConvert, HashMap<Class, String[]> fieldRelevancyLUT, int currentRecursionDepth, int maxRecursionDepth) {

        /*
         * DEBUG
         * Infinite recursion is generally considered bad
         */

        System.out.println(origin.getClass().getName() + "." + IntrusiveModificationHelper.reflectGetFieldName(toConvert) + " [" + currentRecursionDepth + "]");

        /*
         * TEMP VARS
         */

        ArrayList<Byte> toReturn = new ArrayList<>();

        /*
         * ADD BASIC INFO TO RETURN VAR
         */

        // Add appropriate amount of tab characters
        for(int i = 0; i < currentRecursionDepth; i++) toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte("\t") );

        // Add Field Type
        toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte( toConvert.getType().getName() ) );

        // Add Space
        toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte( " " ) );

        // Add Field Name
        toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte( toConvert.getName() ) );

        /*
         * PERFORM RECURSION-LIMITING CHECK
         */

        // Check recursion depth limit; print warning and return if no entry found
        if(currentRecursionDepth > maxRecursionDepth) {
            toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte( " **\\REC_LIM\\**\n" ) );
            return toReturn;
        }

        /*
         * TWEAK LUT TO INCLUDE ENTRIES FROM ICQONReady IF PRESENT
         */

        if( origin instanceof ICQONReady ) {
            fieldRelevancyLUT.put( origin.getClass(), ((ICQONReady) origin).getCQONFieldNames() );
        }

        /*
         * ADD FIELD VALUE TO RETURN VAR
         */

        try {

            if(toConvert.getType().isPrimitive() || toConvert.get(origin) instanceof String) {

                /* WRITE PRIMITIVES AND STRINGS */

                // Add space
                toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte( " " ) );
                // Add field value
                toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte( toConvert.get(origin).toString() ) );
                // Add new line
                toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte( "\n" ) );

            } else {

                /* WRITE NON-STRING OBJECTS USING LUT (IF AVAILABLE) */

                // Return with warning if no LUT provided for current field's type
                if(!fieldRelevancyLUT.containsKey(toConvert.getType())) {
                    toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte( " **\\NO_LUT\\**\n" ) );
                    System.out.println("[CQR] Warning: No LUT provided for " + toConvert.getType() + "; contained data will not be saved to world file!");
                    return toReturn;
                }

                // Add new line
                toReturn.addAll(ByteArrayManipulationUtil.convertStringToArrayListByte("\n"));

                // For each nested field
                for( Field f : IntrusiveModificationHelper.reflectGetAllFields(toConvert.getType()) ) {
                    System.out.println(IntrusiveModificationHelper.reflectGetFieldName(f));
                    // that is listed in the current field's LUT
                    for( String s : fieldRelevancyLUT.get(toConvert.getType()) ) {
                        if(IntrusiveModificationHelper.reflectGetFieldName(f).equals(s)) {
                            // Passes current field as an object, then passes desired nested field and other constant data
                            toReturn.addAll( recursiveConvertFieldToCQON(toConvert.get(origin), f, fieldRelevancyLUT, currentRecursionDepth + 1, maxRecursionDepth) );
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * RETURN
         */

        return toReturn;

    }

    /*
     * CQON --> Object
     */

    public static ArrayList<Field> convertCQONToObject(String fileNameIncludingPath, Object toLoadInto) {

        // NOT YET IMPLEMENTED

        return null;

    }

}
