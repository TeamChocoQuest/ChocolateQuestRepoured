package com.teamcqr.chocolatequestrepoured.util;

import java.lang.reflect.Field;

/**
 * Designed to help minimize repetitive code elsewhere in the mod to account
 * for name differences between dev environment and release environment when
 * using Java's reflection API
 *
 * Use http://export.mcpbot.bspk.rs/stable/ for name reference
 * MCP Name = development environment
 * Searge Name = compiled/release environment
 *
 * @author jdawg3636
 * @version 12/07/2019
 */
public class ForgeReflectionHelper {

    // Prevent instantiation
    private ForgeReflectionHelper() {}

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
        } catch(Exception ex) {}

        // Attempt retrieval using Searge name
        try {
            field = instanceToAccess.getClass().getDeclaredField(seargeFieldName);
        } catch(Exception ex) {}

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

}
