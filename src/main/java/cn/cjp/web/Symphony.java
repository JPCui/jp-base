package cn.cjp.web;

import java.io.IOException;
import java.util.Properties;

import cn.cjp.utils.Logger;
import cn.cjp.utils.PropertiesUtil;

/**
 * symphony.properties 系统常用配置
 * 
 * @author Jinpeng Cui
 *
 */
public class Symphony {

	private static final Logger LOGGER = Logger.getLogger(Symphony.class);

	/**
	 * 用于加载属性
	 */
	public static Properties SYMPHONY_PROPS;

	/**
	 * 静态服务路径
	 */
	private static String staticServerPath;

	/**
	 * 服务器路径
	 */
	private static String serverPath;

	static {
		try {
			PropertiesUtil props = new PropertiesUtil("/symphony.properties");
			SYMPHONY_PROPS = props.getProps();
		} catch (IOException e) {
			LOGGER.error("Not found symphony.properties");
			throw new RuntimeException("Not found symphony.properties");
		}
	}

	public static String getStaticServerPath() {
		if (staticServerPath == null) {
			staticServerPath = SYMPHONY_PROPS.getProperty("staticServerPath");
		}
		return staticServerPath;
	}

	public static void setStaticServerPath(String staticServerPath) {
		Symphony.staticServerPath = staticServerPath;
	}

	public static String getServerPath() {
		if (serverPath == null) {
			serverPath = SYMPHONY_PROPS.getProperty("serverPath");
		}
		return serverPath;
	}

	public static void setServerPath(String serverPath) {
		Symphony.serverPath = serverPath;
	}

}
