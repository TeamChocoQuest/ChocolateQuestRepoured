package com.teamcqr.chocolatequestrepoured.util.data;

import com.teamcqr.chocolatequestrepoured.factions.CQRFaction;

public class ArrayManipulationUtil {
	

	public static CQRFaction[] enlargeFactionArray(CQRFaction[] arrayOld, int newSize) {
		CQRFaction[] array = new CQRFaction[newSize];
		for(int i = 0; i < newSize; i++) {
			if(i < arrayOld.length) {
				array[i] = arrayOld[i];
			}
		}
		return array;
	}
	
}
