package com.teamcqr.chocolatequestrepoured.util.reflection;

import java.lang.reflect.Field;

import com.teamcqr.chocolatequestrepoured.CQRMain;

public class ReflectionField<C, T> {

	private final Field field;

	public ReflectionField(Class<? extends C> clazz, String obfuscatedName, String deobfuscatedName) {
		Field f = null;
		try {
			try {
				f = clazz.getDeclaredField(obfuscatedName);
				f.setAccessible(true);
			} catch (NoSuchFieldException e) {
				f = clazz.getDeclaredField(deobfuscatedName);
				f.setAccessible(true);
			}
		} catch (NoSuchFieldException | SecurityException e) {
			CQRMain.logger.error("Failed to get field from class " + clazz + " for name " + deobfuscatedName, e);
		}
		this.field = f;
	}

	public void set(C obj, T value) {
		try {
			this.field.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException | NullPointerException e ) {
			CQRMain.logger.error("Failed to set field " + this.field.getName() + " for object " + obj + " with value " + value, e);
		}
	}

	public T get(C obj) {
		try {
			return (T) this.field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			CQRMain.logger.error("Failed to get field " + this.field.getName() + " for object " + obj, e);
		}
		return null;
	}

}
