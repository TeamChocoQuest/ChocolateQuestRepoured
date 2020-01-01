package com.teamcqr.chocolatequestrepoured.util.data;

import java.util.AbstractCollection;
import java.util.AbstractMap;

import javax.annotation.Nullable;

public class ArrayCollectionMapManipulationUtil {

	public static boolean checkIfArrayish(Object o) {
		if (o instanceof Object[]) {
			return true;
		}
		if (o instanceof AbstractCollection) {
			return true;
		}
		if (o instanceof AbstractMap) {
			return true;
		}
		return false;
	}

	public static Object[] convertArrayishToArray(Object o) {
		// Vars, only used for AbstractMap
		Object[] keys = new Object[0];
		Object[] values = new Object[0];
		Object[] toReturn = new Object[0];
		// Cases for all Arrayish
		if (o instanceof Object[]) {
			return (Object[]) o;
		}
		if (o instanceof AbstractCollection) {
			return ((AbstractCollection) o).toArray();
		}
		if (o instanceof AbstractMap) {
			keys = ((AbstractMap) o).keySet().toArray();
			values = ((AbstractMap) o).values().toArray();
			for (int i = 0; i < values.length; i++) {
				toReturn[2 * i + 0] = keys[i];
				toReturn[2 * i + 1] = values[i];
			}
			return ((AbstractMap) o).entrySet().toArray();
		}
		// Safety in case param is not a supported array/collection/map
		return new Object[0];
	}

	public static Object[] combineArrays(Object[] a, Object[] b) {
		Object[] toReturn = new Object[a.length + b.length];
		for (int i = 0; i < a.length; i++) {
			toReturn[i] = a[i];
		}
		for (int i = 0; i < b.length; i++) {
			toReturn[i + a.length] = b[i];
		}
		return toReturn;
	}

	public static Object genericAddValueToArrayish(Object arrayishToAddTo, Object toAdd, @Nullable Object keyIfMap) {
		if (arrayishToAddTo instanceof Object[]) {
			combineArrays(((Object[]) arrayishToAddTo), new Object[] { toAdd });
			return arrayishToAddTo;
		} else if (arrayishToAddTo instanceof AbstractCollection) {
			((AbstractCollection<Object>) arrayishToAddTo).add(toAdd);
			return arrayishToAddTo;
		} else if (arrayishToAddTo instanceof AbstractMap) {
			((AbstractMap<Object, Object>) arrayishToAddTo).put(keyIfMap, toAdd);
			return arrayishToAddTo;
		} else {
			return null;
		}
	}

}
