package cn.cjp.utils;

public class Assert {

	public static void assertNotNull(Object obj) {
		if (obj == null) {
			throw new NullPointerException();
		}
	}

}
