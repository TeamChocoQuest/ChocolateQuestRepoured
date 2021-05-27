package team.cqr.cqrepoured.util.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionConstructor<C> {

	private final Constructor<C> constructor;

	public ReflectionConstructor(Class<C> clazz, Class<?>... parameterTypes) {
		Constructor<C> c = null;
		try {
			c = clazz.getDeclaredConstructor(parameterTypes);
			c.setAccessible(true);
		} catch (NoSuchMethodException e) {
			// ignore
		}
		this.constructor = c;
	}

	@SuppressWarnings("unchecked")
	public ReflectionConstructor(String className, Class<?>... parameterTypes) {
		Class<C> clazz;
		try {
			clazz = (Class<C>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			this.constructor = null;
			return;
		}
		Constructor<C> c = null;
		try {
			c = clazz.getDeclaredConstructor(parameterTypes);
			c.setAccessible(true);
		} catch (NoSuchMethodException e) {
			// ignore
		}
		this.constructor = c;
	}

	public C newInstance(Object... initargs) {
		try {
			return this.constructor.newInstance(initargs);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isPresent() {
		return this.constructor != null;
	}

}
