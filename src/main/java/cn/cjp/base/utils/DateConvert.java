package cn.cjp.base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvert {

	private static String defaultFormatString = "yyyy-MM-dd HH:mm:ss";

	public static Date coverToDate(Long time) {
		return new Date(time);
	}

	public static String coverToString(Long time) {
		DateFormat df = new SimpleDateFormat(defaultFormatString);
		return df.format(coverToDate(time));
	}

	public static String coverToString(Date date) {
		DateFormat df = new SimpleDateFormat(defaultFormatString);
		return df.format(date);
	}

	public static String coverToString(String format, Date date) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static Date coverToDate(String str, String form) throws ParseException
	{
		DateFormat df = new SimpleDateFormat(form);
		return df.parse(str);
	}

	/**
	 * 按照默认格式将时间转换为Date类型
	 * <br>格式：yyyy-MM-dd hh:mm:ss
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date convertToDate(String date) throws ParseException
	{
		DateFormat df = new SimpleDateFormat(defaultFormatString);
		return df.parse(date);
	}
	
	/**
	 * 按format格式将字符串转换成date
	 * @param format
	 * @param date
	 * @return {@link Date}
	 * @throws ParseException 
	 */
	public static Date convertToDate(String format,String date) throws ParseException
	{
		DateFormat df = new SimpleDateFormat(format);
		return df.parse(date);
	}
	
	public static void main(String[] args) throws ParseException
	{
	}
	
}
