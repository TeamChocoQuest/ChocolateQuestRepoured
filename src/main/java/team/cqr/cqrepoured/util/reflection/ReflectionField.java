package team.cqr.cqrepoured.util.reflection;

import java.lang.reflect.Field;

import team.cqr.cqrepoured.CQRMain;

public class ReflectionField<T> {

	private final Field field;

	public ReflectionField(Class<?> clazz, String obfuscatedName, String deobfuscatedName) {
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

	public ReflectionField(String className, String obfuscatedName, String deobfuscatedName) {
		Field f = null;
		try {
			Class<?> clazz = Class.forName(className);
			try {
				f = clazz.getDeclaredField(obfuscatedName);
				f.setAccessible(true);
			} catch (NoSuchFieldException e) {
				f = clazz.getDeclaredField(deobfuscatedName);
				f.setAccessible(true);
			}
		} catch (ClassNotFoundException | ClassCastException | NoSuchFieldException | SecurityException e) {
			CQRMain.logger.error("Failed to get field from class " + className + " for name " + deobfuscatedName, e);
		}
		this.field = f;
	}

	public void set(Object obj, T value) {
		try {
			this.field.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			CQRMain.logger.error("Failed to set field " + this.field.getName() + " for object " + obj + " with value " + value, e);
		}
	}

	@SuppressWarnings("unchecked")
	public T get(Object obj) {
		try {
			return (T) this.field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException | ClassCastException e) {
			CQRMain.logger.error("Failed to get field " + this.field.getName() + " for object " + obj, e);
		}
		return null;
	}

}
