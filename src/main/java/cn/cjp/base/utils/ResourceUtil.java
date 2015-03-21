package cn.cjp.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 资源文件工具类（properties）
 *
 */
public class ResourceUtil {
	
	Properties prop = null;
	
	public ResourceUtil(InputStream stream)
	{
		prop = new Properties();
		try {
			prop.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getValue(String key)
	{
		return prop.getProperty(key);
	}
}