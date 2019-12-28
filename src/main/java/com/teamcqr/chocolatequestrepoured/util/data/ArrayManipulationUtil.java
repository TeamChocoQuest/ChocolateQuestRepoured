package com.teamcqr.chocolatequestrepoured.util.data;

public class ArrayManipulationUtil {
	

	public static Object[] enlargeArray(Object[] arrayOld, int newSize) {
		Object[] array = new Object[newSize];
		for(int i = 0; i < newSize; i++) {
			if(i < arrayOld.length) {
				array[i] = arrayOld[i];
			}
		}
		return array;
	}
	
}
