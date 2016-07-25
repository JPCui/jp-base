package cn.cjp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Cui 资源文件工具类（properties）
 * 
 */
public class PropertiesUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PropertiesUtil.class);

	private Map<String, String> params = new HashMap<>();

	public PropertiesUtil(String resource) throws IOException {
		Properties prop = null;
		String stripped = resource.startsWith("/") ? resource.substring(1) : resource;

		InputStream stream = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null) {
			stream = classLoader.getResourceAsStream(stripped);
		}
		if (stream == null) {
			stream = Thread.class.getResourceAsStream(resource);
		}
		if (stream == null) {
			stream = Thread.class.getClassLoader().getResourceAsStream(stripped);
		}
		if (stream == null) {
			throw new IOException(resource + " not found");
		}

		prop = new Properties();
		prop.load(stream);

		for (Object key : prop.keySet()) {
			params.put(key.toString(), prop.getProperty(key.toString()));
		}

	}

	public String getValue(String key) {
		return params.get(key);
	}

	public int getInt(String key, int defaultValue) {
		int value = defaultValue;
		try {
			String s = params.get(key);
			if (s != null) {
				value = Integer.parseInt(params.get(key));
			}
		} catch (NumberFormatException e) {
		}
		return value;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					PropertiesUtil propertiesUtil;
					try {
						propertiesUtil = new PropertiesUtil("redis.properties");
						String host = propertiesUtil.getValue("host");
						if (logger.isInfoEnabled()) {
							logger.info("main(String[]) - String host=" + host); //$NON-NLS-1$
						}
						System.out.println(propertiesUtil.params);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

	}
}