package cn.cjp.utils;

import java.util.Calendar;

public class DateUtil {

	public static long tomorrow() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static long timeToTomorrow() {
		long curr = System.currentTimeMillis();
		return tomorrow() - curr;
	}

}
