package com.teamcqr.chocolatequestrepoured.util.data.IO;

import java.util.HashMap;

public class CQONLookUpTable {

    // Data
    private HashMap<Class, String[]> internalLUT = new HashMap<>();

    // Constructors

    public CQONLookUpTable() {}

    public CQONLookUpTable(HashMap<Class, String[]> LUT) {
        this.internalLUT = LUT;
    }

    // Manipulators
    public void addEntry(Class c, String[] fieldNames) {
        internalLUT.put(c, fieldNames);
    }

    public void removeEntry(Class c) {
        internalLUT.remove(c);
    }

    // Accessors
    public String[] getEntry(Class c) {
        return internalLUT.get(c);
    }

}
