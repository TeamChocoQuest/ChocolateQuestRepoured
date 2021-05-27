package team.cqr.cqrepoured.util.reflection;

import java.lang.reflect.Field;

public class ReflectionField<T> {

	private final Field field;

	public ReflectionField(Class<?> clazz, String obfuscatedName, String deobfuscatedName) {
		Field f = null;
		try {
			f = clazz.getDeclaredField(obfuscatedName);
			f.setAccessible(true);
		} catch (NoSuchFieldException e) {
			try {
				f = clazz.getDeclaredField(deobfuscatedName);
				f.setAccessible(true);
			} catch (NoSuchFieldException e1) {
				// ignore
			}
		}
		this.field = f;
	}

	public ReflectionField(String className, String obfuscatedName, String deobfuscatedName) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			this.field = null;
			return;
		}
		Field f = null;
		try {
			f = clazz.getDeclaredField(obfuscatedName);
			f.setAccessible(true);
		} catch (NoSuchFieldException e) {
			try {
				f = clazz.getDeclaredField(deobfuscatedName);
				f.setAccessible(true);
			} catch (NoSuchFieldException e1) {
				// ignore
			}
		}
		this.field = f;
	}

	public void set(Object obj, T value) {
		try {
			this.field.set(obj, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public T get(Object obj) {
		try {
			return (T) this.field.get(obj);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isPresent() {
		return this.field != null;
	}

}
