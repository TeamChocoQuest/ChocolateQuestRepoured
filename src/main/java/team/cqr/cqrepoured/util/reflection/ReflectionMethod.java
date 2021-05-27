package team.cqr.cqrepoured.util.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionMethod<T> {

	private final Method method;

	public ReflectionMethod(Class<?> clazz, String obfuscatedName, String deobfuscatedName, Class<?>... parameterTypes) {
		Method m = null;
		try {
			m = clazz.getDeclaredMethod(obfuscatedName, parameterTypes);
			m.setAccessible(true);
		} catch (NoSuchMethodException e) {
			try {
				m = clazz.getDeclaredMethod(deobfuscatedName, parameterTypes);
				m.setAccessible(true);
			} catch (NoSuchMethodException e1) {
				// ignore
			}
		}
		this.method = m;
	}

	public ReflectionMethod(String className, String obfuscatedName, String deobfuscatedName, Class<?>... parameterTypes) {
		Class<?> clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			this.method = null;
			return;
		}
		Method m = null;
		try {
			m = clazz.getDeclaredMethod(obfuscatedName, parameterTypes);
			m.setAccessible(true);
		} catch (NoSuchMethodException e) {
			try {
				m = clazz.getDeclaredMethod(deobfuscatedName, parameterTypes);
				m.setAccessible(true);
			} catch (NoSuchMethodException e1) {
				// ignore
			}
		}
		this.method = m;
	}

	@SuppressWarnings("unchecked")
	public T invoke(Object obj, Object... args) {
		try {
			return (T) this.method.invoke(obj, args);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isPresent() {
		return this.method != null;
	}

}
