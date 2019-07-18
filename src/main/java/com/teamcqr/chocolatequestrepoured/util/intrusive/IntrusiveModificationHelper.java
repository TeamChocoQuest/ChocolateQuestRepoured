package com.teamcqr.chocolatequestrepoured.util.intrusive;

import java.lang.reflect.Field;

/**
 * Abstracts away the use of intrusive modding methods such as ASM or Reflection API
 * At the moment this class has very limited functionality, all of which is based upon reflection. Much more is planned in this area.
 *
 * Use http://export.mcpbot.bspk.rs/stable/ for name references
 * MCP Name = development environment
 * Searge Name = compiled/release environment
 *
 * @author jdawg3636
 * @version 14/07/2019
 */
public class IntrusiveModificationHelper {

    /*
     * Setup/Boiler Plate
     *
     * plz ignore
     */

    // Prevent instantiation (all methods are static)
    private IntrusiveModificationHelper() {}

    /*
     * Reflection
     *
     * Uses more resources than standard methods, but shouldn't have any
     * additional consequences in terms of compatibility or stability
     */

    /**
     * Returns a field's value, regardless of accessibility status
     * @return requested field value as instance of Object or null
     */
    public static Object safeGetFieldValue(Object instanceToAccess, String mcpFieldName, String seargeFieldName) {

        // Temp representation of field to return
        Field field = null;

        // Attempt retrieval using MCP name
        try {
            field = instanceToAccess.getClass().getDeclaredField(mcpFieldName);
        } catch(Exception ignore) {}

        // Attempt retrieval using Searge name
        try {
            field = instanceToAccess.getClass().getDeclaredField(seargeFieldName);
        } catch(Exception ignore) {}

        // Access
        try {
            field.setAccessible(true);
            return field.get(instanceToAccess);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Default (this means that something is wrong)
        return null;

    }

    /*
     * Access Transformers
     *
     * Though relatively unintrusive, improper use could theoretically cause
     * stability/compatibility issues.
     */

    // Not yet implemented

    /*
     * ASM
     *
     * Extremely intrusive. Can easily cause stability/compatibility issues in addition to the inherent performance penalties.
     * Should be used only when no other option is available.
     */

    // Not yet implemented

}
