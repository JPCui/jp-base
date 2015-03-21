package cn.cjp.utils;

/*     */
/*     */import java.lang.reflect.Constructor;
/*     */
import java.lang.reflect.InvocationTargetException;

/*     */public class ClassUtil
/*     */{
	/*     */public ClassUtil() {
	}

	/*     */
	/*     */public static Object create(String className)
			throws IllegalAccessException, ClassNotFoundException
	/*     */{
		/* 19 */Object object = null;
		/*     */
		/*     */
		/* 22 */Thread t = Thread.currentThread();
		/* 23 */ClassLoader cl = t.getContextClassLoader();
		/*     */
		/*     */try
		/*     */{
			/* 27 */object = cl.loadClass(className).newInstance();
			/*     */}
		/*     */catch (InstantiationException e) {
			/*     */}
		/*     */
		/*     */
		/* 41 */return object;
		/*     */}

	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */public static Object create(String className,
			Object[] constructorParameter)
	/*     */throws Exception
	/*     */{
		/* 61 */@SuppressWarnings("rawtypes")
		Constructor[] constructors = (Constructor[]) null;
		/*     */
		/*     */
		/* 64 */Thread t = Thread.currentThread();
		/* 65 */ClassLoader cl = t.getContextClassLoader();
		/*     */
		/*     */try
		/*     */{
			/* 69 */constructors = cl.loadClass(className).getConstructors();
			/*     */}
		/*     */catch (SecurityException e) {
			/*     */}
		/*     */
		/*     */
		/* 79 */for (int i = 0; i < constructors.length; i++)
		/*     */{
			/*     */
			/* 82 */Object object = null;
			/*     */
			/*     */try
			/*     */{
				/* 86 */object = constructors[i]
						.newInstance(constructorParameter);
				/*     */}
			/*     */catch (IllegalArgumentException e) {
				/*     */continue;
				/*     */}
			/*     */catch (InstantiationException e) {
				/*     */}
			/*     */catch (IllegalAccessException e) {
				/*     */}
			/*     */catch (InvocationTargetException e) {
				/*     */}
			/*     */
			/*     */
			/* 102 */if (object != null) {
				/* 103 */return object;
				/*     */}
			/*     */}
		/*     */
		/*     */
		/* 108 */throw new IllegalArgumentException("class name is "
				+ className);
		/*     */}
}
