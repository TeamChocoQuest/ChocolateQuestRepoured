package com.teamcqr.chocolatequestrepoured.util.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.teamcqr.chocolatequestrepoured.CQRMain;

public class ReflectionMethod<C, T> {

	private final Method method;

	public ReflectionMethod(Class<? extends C> clazz, String obfuscatedName, String deobfuscatedName, Class<?>... parameterTypes) {
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

	public T invoke(C obj, Object... args) {
		try {
			return (T) this.method.invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			CQRMain.logger.error("Failed to invoke method " + this.method.getName() + " for object " + obj + " with parameters " + args, e);
		}
		return null;
	}

}
