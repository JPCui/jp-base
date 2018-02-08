package cn.cjp.core.cache.file;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.cjp.core.cache.Cache;
import cn.cjp.core.cache.CacheCallback;
import cn.cjp.utils.FileUtil;
import cn.cjp.utils.JacksonUtil;
import cn.cjp.utils.StringUtil;
import lombok.Data;

@Data
public class FileCache implements Cache {

	private String filePath = "/data/cache";

	public FileCache() {
		File path = new File(filePath);
		if (!path.exists()) {
			path.mkdirs();
		}
	}

	private File file(String s) {
		String sub = null;
		try {
			sub = URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		String fileName = filePath.concat("/").concat(sub);
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		return file;
	}

	@Override
	public void set(String key, Object value, long expireTime) {
		FileUtil.write(JacksonUtil.toJson(value), file(key), false);
	}

	@Override
	public <T> T get(Class<T> c, String key) {
		String v = FileUtil.readLine(file(key));
		if (StringUtil.isEmpty(v)) {
			return null;
		}
		return JacksonUtil.fromJsonToObj(v, c);
	}

	@Override
	public Long delete(String key) {
		FileUtil.delete(file(key));
		return 1L;
	}

	@Override
	public Long delete(Collection<String> key) {
		key.forEach(k -> {
			delete(k);
		});
		return 1L;
	}

	@Override
	public Long sadd(String key, Object... values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> List<T> smembers(Class<T> c, String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T execute(CacheCallback<T> callback) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDomainKey(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> keys(String k) {

		File path = new File(filePath);
		if (path.exists()) {
			String[] keys = path.list();
			Set<String> hashSet = new HashSet<>();
			Arrays.asList(keys).forEach(key -> {
				if (key.startsWith(k)) {
					hashSet.add(key);
				}
			});
			return hashSet;
		}

		return Collections.emptySet();
	}

}
