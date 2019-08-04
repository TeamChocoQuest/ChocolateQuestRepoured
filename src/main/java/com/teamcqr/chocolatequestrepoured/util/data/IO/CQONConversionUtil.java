package com.teamcqr.chocolatequestrepoured.util.data.IO;

import com.teamcqr.chocolatequestrepoured.intrusive.IntrusiveModificationHelper;
import com.teamcqr.chocolatequestrepoured.util.data.ByteArrayManipulationUtil;
import com.teamcqr.chocolatequestrepoured.util.data.PrimitiveManipulationUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

import java.lang.reflect.Field;
import java.util.*;

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
 * @version 03.08.2019
 */
public class CQONConversionUtil {

    /*
     * User Util
     */

    // Must be manually applied - provided for programmer convenience
    public static HashMap<Class, String[]> getDefaultRelevancyLUT() {

        HashMap<Class, String[]> toReturn = new HashMap<>();

        toReturn.put(BlockPos.class, new String[]{"x", "y", "z"});
        toReturn.put(WorldServer.class, new String[]{"worldInfo"});
        toReturn.put(WorldInfo.class, new String[]{"dimension", "levelName"});

        return toReturn;

    }

    /*
     * Internal Util
     * TODO Create a dedicated util class for this in a more sensible location
     */

    private static boolean checkIfArrayish(Object o) {
        if(o instanceof Object[])
            return true;
        if(o instanceof AbstractCollection)
            return true;
        if(o instanceof AbstractMap)
            return true;
        return false;
    }

    private static Object[] convertArrayishToArray(Object o) {
        // Cases for all Arrayish
        if(o instanceof Object[])
            return (Object[])o;
        if(o instanceof AbstractCollection)
            return ((AbstractCollection)o).toArray();
        if(o instanceof AbstractMap)
            return ((AbstractMap)o).entrySet().toArray();
        // Safety in case param is not a supported array/collection/map
        return new Object[] {o};
    }

    /*
     * Object --> CQON
     */

    /**
     * Serializes a given Object, regardless of type
     * Requires less configuration when used on Objects that properly implement ICQONReady
     * @param toSerialize Object to serialize
     * @param LUT Look up table to determine if a given field should be saved
     * @param currentRecursionDepth Set this to zero if manually calling (used internally for recursive calls)
     * @return CQON Representation of the Provided Object
     */
    public static ArrayList<Byte> convertObjectToCQON(Object toSerialize, String toSerializeName, CQONLookUpTable LUT, int currentRecursionDepth) {

        // Temp Vars
        ArrayList<Byte> toReturn = new ArrayList<>();

        // Update LUT to include data from ICONReady
        if(toSerialize instanceof ICQONReady) {
            LUT.addEntry(toSerialize.getClass(), ((ICQONReady)toSerialize).getCQONFieldNames());
        }

        // Add Indentation
        for(int i = 0; i < currentRecursionDepth; i++) toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte("\t") );

        // Add Class Name
        toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte(toSerialize.getClass().getName()) );
        // Add Space
        toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte(" ") );
        // Add Object Name
        toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte(toSerializeName) );

        // Check if primitive
        if( PrimitiveManipulationUtil.isPrimitive(toSerialize) || toSerialize instanceof String ) {
            // Add Space
            toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte(" ") );
            // Add Value
            toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte(toSerialize.toString()) );
            // Add New Line
            toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte("\n") );
        }

        // Otherwise check if array
        else if( checkIfArrayish(toSerialize) ) {
            // Add space, opening brace, and new line
            toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte(" {\n") );
            // Make recursive calls for each element in the array
            Object[] array = convertArrayishToArray( toSerialize );
            for(int i = 0; i < array.length; i++) convertObjectToCQON(array[i], toSerializeName + "[" + i + "]", LUT, currentRecursionDepth + 1);
            // Add Indentation
            for(int i = 0; i < currentRecursionDepth; i++) toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte("\t") );
            // Add closing brace and new line
            toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte("}\n") );
        }

        // Otherwise make recursive calls for values in LUT
        else {

            // Check if LUT contains entry for current type
            if(LUT.getEntry(toSerialize.getClass()) != null) {
                // Add space, opening bracket, and new line
                toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte(" [\n") );
                // Make recursive call for all contained fields listed in LUT entry
                for(String fieldNameFromLUT : LUT.getEntry(toSerialize.getClass())) {
                    for(Field fieldFromProvidedInstance : IntrusiveModificationHelper.reflectGetAllFields(toSerialize)) {
                        if(IntrusiveModificationHelper.reflectGetFieldName(fieldFromProvidedInstance).equals(fieldNameFromLUT)) {
                            toReturn.addAll( convertObjectToCQON(IntrusiveModificationHelper.reflectGetFieldValue(toSerialize, fieldFromProvidedInstance), IntrusiveModificationHelper.reflectGetFieldName(fieldFromProvidedInstance), LUT, currentRecursionDepth + 1) );
                        }
                    }
                }
                // Add Indentation
                for(int i = 0; i < currentRecursionDepth; i++) toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte("\t") );
                // Add closing bracket and new line
                toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte("]\n") );
            }

            // Otherwise add appropriate flag to output
            else {
                // Add space, flag, and then new line
                toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte(" **\\NO_LUT\\**\n") );
            }

        }

        // Return
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
