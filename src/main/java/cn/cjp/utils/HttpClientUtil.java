package cn.cjp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

	static String loginUrl = "http://local.betago2016.com:8080/beta-core/user/login";

	CloseableHttpClient httpClient;

	public HttpClientUtil() {
		HttpClientBuilder builder = HttpClientBuilder.create();
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
		builder.setConnectionManager(manager);
		// builder.setConnectionReuseStrategy(ConnectionReuseStrategy)

		// HttpHost proxy = new HttpHost("localhost", 8888);
		// builder.setProxy(proxy);
		httpClient = builder.build();
	}

	public CloseableHttpResponse get(String url, Map<String, String> params) throws ParseException, IOException {
		List<NameValuePair> nvs = new ArrayList<>();
		for (String key : params.keySet()) {
			nvs.add(new BasicNameValuePair(key, params.get(key)));
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvs, "UTF-8");
		HttpGet httpGet = new HttpGet(url + "?" + EntityUtils.toString(entity, Charset.defaultCharset()));
		CloseableHttpResponse response = httpClient.execute(httpGet);
		return response;
	}

	public CloseableHttpResponse post(String url, Map<String, String> params)
			throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		List<NameValuePair> nvs = new ArrayList<>();
		for (String key : params.keySet()) {
			nvs.add(new BasicNameValuePair(key, params.get(key)));
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvs, "UTF-8");
		post.setEntity(entity);

		CloseableHttpResponse response = httpClient.execute(post);
		return response;
	}

	public void test() throws IOException, URISyntaxException {

		Map<String, String> params = new HashMap<>();
		params.put("userid", "103");
		CloseableHttpResponse response = post(loginUrl, params);
		System.out.println(response.getStatusLine());
		InputStream is = response.getEntity().getContent();
		String content = StreamUtil.copyToString(is, Charset.defaultCharset());
		System.out.println(content);

		params.put("content", "哈哈");
		params.put("userid", "103");
		params.put("orderid", "568");
		params.put("sessionid", "1498804810u103");
		response = post("http://local.betago2016.com:8080/beta-core/chat", params);
		System.out.println(response.getStatusLine());
		is = response.getEntity().getContent();
		content = StreamUtil.copyToString(is, Charset.defaultCharset());
		System.out.println(content);

	}

}
