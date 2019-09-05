package com.teamcqr.chocolatequestrepoured.util.data.IO;

import com.teamcqr.chocolatequestrepoured.intrusive.IntrusiveModificationHelper;
import com.teamcqr.chocolatequestrepoured.util.data.ArrayCollectionMapManipulationUtil;
import com.teamcqr.chocolatequestrepoured.util.data.ByteArrayManipulationUtil;
import com.teamcqr.chocolatequestrepoured.util.data.PrimitiveManipulationUtil;
import net.minecraft.entity.Entity;
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
     * SERIALIZATION
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
        for(int i = 0; i < currentRecursionDepth; i++) {
            toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte("\t") );
        }

        // Check if null
        try {
            toSerialize.getClass();
        } catch (NullPointerException e) {
            // Add null flag and new line
            toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte("**NULL**\n") );
            // Return
            return toReturn;
        }

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
        else if(ArrayCollectionMapManipulationUtil.checkIfArrayish(toSerialize) ) {
            // Add space, opening brace, and new line
            toReturn.addAll( ByteArrayManipulationUtil.convertStringToArrayListByte(" {\n") );
            // Iterate through each element in the array
            Object[] array = ArrayCollectionMapManipulationUtil.convertArrayishToArray( toSerialize );
            for(int i = 0; i < array.length; i++) {
                // Make recursive call
                toReturn.addAll( convertObjectToCQON(array[i], toSerializeName + "[" + i + "]", LUT, currentRecursionDepth + 1) );
            }
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
     * DESERIALIZATION
     */

    public static HashMap<String, Object> convertCQONToObject(ArrayList<Byte> serializedData) {

        // Vars
        ArrayList<String> linesFromInput = new ArrayList<>();
        ArrayList<Byte> buffer = new ArrayList<>();

        // Convert serializedData into an ArrayList of Strings, with each String value representing a line from the incoming CQON
        for(Byte b : serializedData) {
            // If not a newline char, add to buffer
            if(b != '\n') {
                buffer.add(b);
            }
            // Otherwise transfer buffer contents to linesFromInput and clear buffer
            else {
                linesFromInput.add(ByteArrayManipulationUtil.convertArrayListByteToString(buffer));
                buffer = new ArrayList<>();
            }
        }

        // Add remaining data (likely blank line at end of file)
        if(buffer.size() != 0) {
            linesFromInput.add(ByteArrayManipulationUtil.convertArrayListByteToString(buffer));
        }

        // Recursively parse
        HashMap<String, Object> temp = recursiveConvertCQONToObject(linesFromInput, 0);
        System.out.println("FINAL OUT: " + temp);
        return temp;

    }

    // Internal, recursive portion of deserialization method
    // Separated from public-facing method for performance reasons
    private static HashMap<String, Object> recursiveConvertCQONToObject(ArrayList<String> linesFromInput, int startingLineNumber) {

        // Vars
        HashMap<String, Object> toReturn = new HashMap<>();
        int currentLineNumber = startingLineNumber;

        // Iterate through lines
        for(/**/; currentLineNumber < linesFromInput.size(); currentLineNumber++) {

            /* Extract Tokens */

            // Line-specific values
            StringBuilder[] tokens = new StringBuilder[3];
            int tokenIndex = 0;

            // Initialize StringBuilders
            for(int i = 0; i < tokens.length; i++) {
                tokens[i] = new StringBuilder();
            }

            // Separate line into 3 tokens, corresponding to type, name, and value respectively.
            for(char c : linesFromInput.get(currentLineNumber).toCharArray()) {
                if(c == '\t') {
                    // ignore - tabs exists solely for readability
                } else if (c == ' ') {
                    tokenIndex++;
                } else {
                    tokens[tokenIndex].append(c);
                }
            }

            // DEBUG OUTPUT
            //*
            System.out.println("RECURSIVE CQON DESERIALIZATION [" + startingLineNumber + ", " + currentLineNumber + "/" + (linesFromInput.size() - 1) + "]");
            System.out.println("TOKENS:");
            System.out.println(tokens[0].toString());
            System.out.println(tokens[1].toString());
            System.out.println(tokens[2].toString());
            //*/

            /* Deserialize */

            // Recursion check
            if(tokens[0].toString().equals("]") || tokens[0].toString().equals("}") || linesFromInput.get(currentLineNumber).equals("")) {
                toReturn.put("__CQON__currentLineNumber", currentLineNumber);
            }
            // Object
            else if(tokens[2].toString().equals("[")) {

                // Return var
                Object toAddToReturn = null;

                // Instantiate return object
                try {
                    toAddToReturn = IntrusiveModificationHelper.getInstanceOfClass( Class.forName( tokens[0].toString() ) );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(toAddToReturn);
                System.out.println(toAddToReturn.getClass());

                // Set each value recursively
                boolean continueRecursion = true;
                HashMap<String, Object> parsed = null;
                // Loop until end of block
                while(continueRecursion) {
                    // Make recursive call (no parameter manipulation, already incremented)
                    parsed = recursiveConvertCQONToObject(linesFromInput, currentLineNumber + 1);
                    try{
                        System.out.println("Obj Parsed: " + parsed);
                        // If valid value, set appropriate field value and continue loop (try block to catch NPE)
                        if(parsed.keySet().toArray().length != 0) {
                            // Manually increment containing for loop
                            currentLineNumber = (Integer)parsed.get("__CQON__currentLineNumber");
                            parsed.remove("__CQON__currentLineNumber");
                            System.out.println("All fields in toAddToReturn: " + IntrusiveModificationHelper.reflectGetAllFieldNames(toAddToReturn));
                            System.out.println((String)(parsed.keySet().toArray()[0]) + " = " + parsed.get(parsed.keySet().toArray()[0]));
                            toAddToReturn = IntrusiveModificationHelper.reflectSetFieldValue(toAddToReturn, IntrusiveModificationHelper.reflectGetField(toAddToReturn, new String[] {(String)(parsed.keySet().toArray()[0])}), parsed.get(parsed.keySet().toArray()[0]));
                        }
                        // Otherwise end loop
                        else {
                            continueRecursion = false;
                        }
                    } catch (NullPointerException ignored) {
                        ignored.printStackTrace();
                        continueRecursion = false;
                    }
                }

                // Add to return var
                toReturn.put( tokens[1].toString(), toAddToReturn );

            }
            // Array
            else if(tokens[2].toString().equals("{")) {

                // Vars
                Object toAddToReturn = null;
                ArrayList<Object> temp = new ArrayList<>();
                HashMap<String, Object> parsed = null;
                HashMap<String, Object> parsed2 = null;
                boolean continueRecursion = true;

                // Instantiate return object
                try {
                    toAddToReturn = IntrusiveModificationHelper.getInstanceOfClass( Class.forName( tokens[0].toString() ) );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Loop until end of block, adding all values to temp
                while(continueRecursion) {
                    // Make recursive call (no parameter manipulation, already incremented)
                    parsed = recursiveConvertCQONToObject(linesFromInput, currentLineNumber + 1);
                    try {
                        // If valid value, set appropriate field value and continue loop (try block to catch NPE)
                        if(parsed.values().toArray().length != 0 && parsed.values().toArray()[0] != null) {
                            // Manually increment containing for loop
                            System.out.println( "__CQON__currentLineNumber = " + parsed.get("__CQON__currentLineNumber") );
                            currentLineNumber = (Integer)parsed.get("__CQON__currentLineNumber");
                            parsed.remove("__CQON__currentLineNumber");
                            temp.add(parsed.values().toArray()[0]);
                        }
                        // Otherwise end loop
                        else {
                            continueRecursion = false;
                        }
                    } catch (NullPointerException ignored) {
                        continueRecursion = false;
                    }
                }

                // Add to toAddToReturn var (Implementation is type-specific)
                if(toAddToReturn instanceof Object[]) {
                    toAddToReturn = ArrayCollectionMapManipulationUtil.convertArrayishToArray(temp);
                }
                else if(toAddToReturn instanceof AbstractCollection) {
                    ((AbstractCollection)toAddToReturn).addAll(temp);
                }
                else if(toAddToReturn instanceof AbstractMap) {
                    for(int i = 0; i < temp.size(); i++) {
                        ((AbstractMap)toAddToReturn).put(temp.get(2 * i), temp.get(2 * i + 1));
                    }
                }

                // Add to return var
                toReturn.put( tokens[1].toString(), toAddToReturn );

            }
            // Primitive/String
            else {

                // Attempt to instantiate prim using PrimitiveManipulationUtil
                Object fromPrimitiveManipulationUtil = PrimitiveManipulationUtil.getPrimFromString(tokens[0].toString(), tokens[2].toString());

                // If successful, put into return var
                if(fromPrimitiveManipulationUtil != null) {
                    toReturn.put(tokens[1].toString(), fromPrimitiveManipulationUtil);
                }
                // Otherwise, assume value is a String and return directly
                else {
                    toReturn.put(tokens[1].toString(), tokens[2].toString());
                }

            }

        }

        return toReturn;

    }

    /*
     * UTIL
     */

    // Must be manually applied - only provides LUTs for classes in dependencies such as Forge, Minecraft, Java, etc.
    // Any classes under our control should implement the ICQONReady interface to avoid the need for an entry here.
    public static CQONLookUpTable getDefaultRelevancyLUT() {

        CQONLookUpTable toReturn = new CQONLookUpTable();

        // Minecraft - MUST INCLUDE BOTH MCP AND SEARGE NAMING SCHEMES
        toReturn.addEntry(BlockPos.class, new String[]{"x", "field_177962_a", "y", "field_177960_b", "z", "field_177961_c"});
        toReturn.addEntry(WorldServer.class, new String[]{"worldInfo", "field_72986_A"});
        toReturn.addEntry(WorldInfo.class, new String[]{"dimension", "field_76105_j"});
        toReturn.addEntry(Entity.class, new String[]{"entityUniqueID", "field_96093_i"});
        // Java
        toReturn.addEntry(UUID.class, new String[]{"mostSigBits", "leastSigBits"});

        return toReturn;

    }

}
