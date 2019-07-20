package com.teamcqr.chocolatequestrepoured.intrusive;

import java.lang.reflect.Field;

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
     * Reflection
     *
     * Uses more resources than usual, but shouldn't have any
     * inherent consequences in terms of compatibility or stability
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
     * ASM
     *
     * Extremely intrusive and highly discouraged by Forge developers. Can easily
     * cause stability/compatibility issues in addition to the inherent performance
     * penalties. Should be used only when no other options are available.
     */

    // Not yet implemented

}
