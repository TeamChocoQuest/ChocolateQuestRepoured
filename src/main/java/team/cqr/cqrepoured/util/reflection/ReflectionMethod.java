package team.cqr.cqrepoured.util.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import team.cqr.cqrepoured.CQRMain;

public class ReflectionMethod<T> {

	private final Method method;

	public ReflectionMethod(Class<?> clazz, String obfuscatedName, String deobfuscatedName, Class<?>... parameterTypes) {
		Method m = null;
		try {
			try {
				m = clazz.getDeclaredMethod(obfuscatedName, parameterTypes);
				m.setAccessible(true);
			} catch (NoSuchMethodException e) {
				m = clazz.getDeclaredMethod(deobfuscatedName, parameterTypes);
				m.setAccessible(true);
			}
		} catch (NoSuchMethodException | SecurityException e) {
			CQRMain.logger.error("Failed to get method from class " + clazz + " for name " + deobfuscatedName, e);
		}
		this.method = m;
	}

	public ReflectionMethod(String className, String obfuscatedName, String deobfuscatedName, Class<?>... parameterTypes) {
		Method m = null;
		try {
			Class<?> clazz = Class.forName(className);
			try {
				m = clazz.getDeclaredMethod(obfuscatedName, parameterTypes);
				m.setAccessible(true);
			} catch (NoSuchMethodException e) {
				m = clazz.getDeclaredMethod(deobfuscatedName, parameterTypes);
				m.setAccessible(true);
			}
		} catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | SecurityException e) {
			CQRMain.logger.error("Failed to get method from class " + obfuscatedName + " for name " + deobfuscatedName, e);
		}
		this.method = m;
	}

	@SuppressWarnings("unchecked")
	public T invoke(Object obj, Object... args) {
		try {
			return (T) this.method.invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassCastException e) {
			CQRMain.logger.error("Failed to invoke method " + this.method.getName() + " for object " + obj + " with parameters " + args, e);
		}
		return null;
	}

}
