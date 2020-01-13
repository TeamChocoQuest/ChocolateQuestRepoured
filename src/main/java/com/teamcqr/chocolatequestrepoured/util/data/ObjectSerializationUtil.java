package com.teamcqr.chocolatequestrepoured.util.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectSerializationUtil {

	public static byte[] writeSerializableToByteArray(Serializable objToWrite) {
		ByteArrayOutputStream toReturn = new ByteArrayOutputStream();
		try {
			(new ObjectOutputStream(toReturn)).writeObject(objToWrite);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toReturn.toByteArray();
	}

	public static Object readObjectFromByteArray(byte[] serializedObj) {
		ByteArrayInputStream toReturn = new ByteArrayInputStream(serializedObj);
		try {
			return new ObjectInputStream(toReturn).readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
