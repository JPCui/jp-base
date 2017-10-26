package cn.cjp.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
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

	CloseableHttpClient httpClient;

	public HttpClientUtil() {
		HttpClientBuilder builder = HttpClientBuilder.create();
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
		builder.setConnectionManager(manager);
		// builder.setConnectionReuseStrategy(ConnectionReuseStrategy)

		// HttpHost proxy = new HttpHost("160.16.94.228", 80);
		// builder.setProxy(proxy);
		builder.setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(30_000)
				.setConnectionRequestTimeout(30_000).setSocketTimeout(30_000).build());
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

	}

}
