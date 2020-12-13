package team.cqr.cqrepoured.util.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import team.cqr.cqrepoured.CQRMain;

public class ReflectionConstructor<C> {

	private final Constructor<C> constructor;

	public ReflectionConstructor(Class<C> clazz, Class<?>... parameterTypes) {
		Constructor<C> c = null;
		try {
			c = clazz.getDeclaredConstructor(parameterTypes);
			c.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			CQRMain.logger.error("Failed to get constructor from class " + clazz, e);
		}
		this.constructor = c;
	}

	@SuppressWarnings("unchecked")
	public ReflectionConstructor(String className, Class<?>... parameterTypes) {
		Constructor<C> c = null;
		try {
			Class<C> clazz = (Class<C>) Class.forName(className);
			c = clazz.getDeclaredConstructor(parameterTypes);
			c.setAccessible(true);
		} catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | SecurityException e) {
			CQRMain.logger.error("Failed to get constructor from class " + className, e);
		}
		this.constructor = c;
	}

	public C newInstance(Object... initargs) {
		try {
			return this.constructor.newInstance(initargs);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			CQRMain.logger.error("Failed to create new instance for class " + this.constructor.getName() + " with parameters " + initargs, e);
		}
		return null;
	}

}
