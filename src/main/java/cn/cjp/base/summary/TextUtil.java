package cn.cjp.base.summary;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本工具类
 */
public class TextUtil {

	/**
	 * 分句
	 * @param str
	 * @return
	 */
	public static List<String> divSentence(String str)
	{
		String tag = "["
				+ "。 "	+ "！ "	+ "？ "
				+ ". "	+ "! "	+ "? "
				+ "]";
		String[] strs = str.split(tag);
		List<String> strsList = new ArrayList<String>();
		for(int i=0; i<strs.length; i++)
		{
			strsList.add(strs[i]);
		}
		return strsList;
	}
	
	
	
	
	
	
	
	
	
	
}
