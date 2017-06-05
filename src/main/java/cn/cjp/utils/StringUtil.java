package cn.cjp.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

	private static Logger logger = Logger.getLogger(StringUtil.class);

	public static final String EMPTY = "";

	public static final String EMOJI_REGEX = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";

	public static final String LINE_SEPARATOR = "\r\n";

	public static boolean containsEmoji(String source) {
		if (StringUtil.isEmpty(source)) {
			return false;
		}
		Pattern emoji = Pattern.compile(EMOJI_REGEX, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
		Matcher emojiMatcher = emoji.matcher(source);
		return emojiMatcher.find();
	}

	public static String filterEmoji(String source) {
		if (source != null) {
			Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
					Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
			Matcher emojiMatcher = emoji.matcher(source);
			if (emojiMatcher.find()) {
				source = emojiMatcher.replaceAll("*");
				return source;
			}
			return source;
		}
		return source;
	}

	public static String filterEmoji(String s, String replace) {
		if (StringUtils.isNotBlank(s)) {
			return s.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", replace);
		} else {
			return s;
		}
	}
	
	/**
	 * 去除 UTF8MB4 字符
	 * @param text
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String filterUtf8mb4(String text) throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("UTF-8");
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}
			b += 256;
			if ((b ^ 0xC0) >> 4 == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			} else if ((b ^ 0xE0) >> 4 == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			} else if ((b ^ 0xF0) >> 4 == 0) {
				i += 4;
			} else {
				i++;
			}
		}
		buffer.flip();
		return new String(buffer.array(), "utf-8");
	}

	public static String combine(Object[] args, String split) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			stringBuilder.append(i == 0 ? arg : split + arg);
		}
		return stringBuilder.toString();
	}

	public static String combine(List<?> args, String split) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < args.size(); i++) {
			Object arg = args.get(i);
			stringBuilder.append(i == 0 ? arg : split + arg);
		}
		return stringBuilder.toString();
	}

	public static Integer[] split(String str, String regex) {
		if (StringUtils.isEmpty(str)) {
			return new Integer[] {};
		}
		String[] strArray = str.split(regex);
		Integer[] intArray = new Integer[strArray.length];
		for (int i = 0; i < strArray.length; i++) {
			if (StringUtils.isEmpty(strArray[i])) {
				continue;
			}
			intArray[i] = Integer.parseInt(strArray[i]);
		}
		return intArray;
	}

	public static Integer[] splitIgnoreEmpty(String str, String split) {
		if (StringUtils.isEmpty(str)) {
			return new Integer[] {};
		}
		String[] strArray = str.trim().split(split);
		Integer[] intArray = new Integer[strArray.length];
		for (int i = 0; i < strArray.length; i++) {
			if (StringUtils.isEmpty(strArray[i])) {
				continue;
			}
			intArray[i] = Integer.parseInt(strArray[i].trim());
		}
		return intArray;
	}

	public static boolean isEmpty(String str) {
		return (str == null || "".equals(str));
	}

	public static String toString(Object message, String defaultValue) {
		if (message instanceof String) {
			return message.toString();
		}
		if (message != null) {
			return message.toString();
		}
		return defaultValue;
	}

	public static List<String> splitToStr(String str, String regex, boolean ignoreEmpty) {
		if (StringUtil.isEmpty(str)) {
			return Collections.emptyList();
		}
		String[] subStrs = str.split(regex);
		List<String> subStrList = Arrays.asList(subStrs);
		List<String> result = new ArrayList<>();
		for (String subStr : subStrList) {
			if (ignoreEmpty && StringUtil.isEmpty(subStr)) {
				continue;
			}
			result.add(subStr);
		}
		return result;
	}

	public static String fillEmpty(String str, int num, int length) {
		int strLength = str.length();
		for (int i = strLength; i < length; i++) {
			str = num + str;
		}
		return str;
	}

	public static void main(String[] args) {
		logger.error("test");
	}

}
