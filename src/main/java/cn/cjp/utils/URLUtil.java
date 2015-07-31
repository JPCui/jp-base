package cn.cjp.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtil {

	/**
	 * 返回去掉锚点的URL，即去掉'#'及'#'后面的字符串
	 * 
	 * @param urlStr
	 *            原URL
	 * @return
	 */
	public static String url(String urlStr) {
		URL url = null;
		try {
			url = new URL(urlStr);
			String pureUrl = url.getProtocol() + "://" + url.getHost()
					+ url.getPath();
			if (url.getQuery() != null) {
				pureUrl += "?" + url.getQuery();
			}
			return pureUrl;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取域名
	 * 
	 * @param urlStr
	 * @return
	 */
	public static String getHost(String urlStr) {
		try {
			return new URL(urlStr).getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
