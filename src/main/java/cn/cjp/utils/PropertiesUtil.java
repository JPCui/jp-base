package cn.cjp.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
	private static final Logger LOGGER = Logger.getLogger(PropertiesUtil.class);

	private Map<String, String> params = new HashMap<>();

	private Properties props = null;

	public PropertiesUtil(String resource) throws IOException {
		props = new Properties();
		props.load(getInputStream(resource));

		for (Object key : props.keySet()) {
			params.put(key.toString(), props.getProperty(key.toString()));
		}
	}

	private static InputStream getInputStream(String resource) throws IOException {
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
		return stream;
	}

	private static File getFile(String resource) {
		String stripped = resource.startsWith("/") ? resource.substring(1) : resource;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL res = classLoader.getResource(stripped);
		return new File(res.getFile());
	}

	public static void rewrite(String resource, Map<String, String> params) throws IOException {
		File file = getFile(resource);

		// 获取已有参数
		Properties props = new Properties();
		props.load(getInputStream(resource));
		for (Object key : props.keySet()) {
			if (!params.containsKey(key)) {
				params.put(key.toString(), props.getProperty(key.toString()));
			}
		}

		StringBuilder content = new StringBuilder();
		for (String key : params.keySet()) {
			content.append(String.format("%s=%s\r\n", key, params.get(key)));
		}

		FileUtil.write(content.toString(), file, false);
	}

	public Properties getProps() {
		return props;
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
		PropertiesUtil util = new PropertiesUtil("test.properties");
		System.out.println(util.getParams());

		LOGGER.info(getFile("test.properties"));

		Map<String, String> params = new HashMap<>();
		params.put("b", "22");
		params.put("c", "3");
		rewrite("test.properties", params);

	}
}