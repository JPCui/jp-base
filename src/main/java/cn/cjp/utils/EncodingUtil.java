package cn.cjp.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

/**
 * 编码工具类
 *
 */
public class EncodingUtil {
	
	static String[] codes = {
			"GB2312", "GBK",
			"UTF-8", "UTF-16",
			"Unicode", "ISO-8859-1",
	};

	/**
	 * 判断编码
	 * @param str
	 * @return
	 */
	public static String judgeCoding(String str)
	{
		if(StringUtils.isBlank(str))return "no judge result";
		
		for(String code : codes)
		{
			try {
				if(str.equals(new String(str.getBytes(code), code)))
					return code;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "no judge result";
	}
	
	/**
	 * 去除字节码为65536的字符
	 * @param str 源字符串
	 * @return 去除字节码为65536的字符后的字符串
	 */
	public static String removeByte65535(String str)
	{
		String newStr = "";
		char[] chs = str.toCharArray();
		int preIndex = 0;	// 上一次截取的位置
		
		for(int i=0; i<chs.length; i++)
		{
			char ch = chs[i];
			if((int)ch == 0xFFFF)
			{
				newStr += str.substring(preIndex, i);
				preIndex = i+1;
			}
		}
		newStr += str.substring(preIndex);
		
		return newStr;
	}
	
}
