package cn.cjp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 将含有unicode码的字符串转换为正常字符串
 * @author REAL
 *
 */
public class CodeUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		System.out.println(unicodeToString("123abc"));

	}
	public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");    
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");    
        }
        str = str.replaceAll("\\\\", "");
        return str;
    }

}
