package com.teamcqr.chocolatequestrepoured.util.data;

import java.util.AbstractCollection;
import java.util.AbstractMap;

public class ArrayCollectionMapManipulationUtil {

    public static boolean checkIfArrayish(Object o) {
        if(o instanceof Object[])
            return true;
        if(o instanceof AbstractCollection)
            return true;
        if(o instanceof AbstractMap)
            return true;
        return false;
    }

    public static Object[] convertArrayishToArray(Object o) {
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

}
