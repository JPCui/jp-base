package cn.cjp.core.cache.file;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
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
import cn.cjp.utils.Logger;
import cn.cjp.utils.SerializeHelper;
import lombok.Data;

@Data
public class FileCache implements Cache {

    private static final Logger LOGGER = Logger.getLogger(FileCache.class);

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
        try {
            FileUtils.writeByteArrayToFile(file(key), SerializeHelper.serialize(value), false);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public <T> T get(Class<T> c, String key) {
        byte[] v = null;
        try {
            v = FileUtils.readFileToByteArray(file(key));
            return (T) SerializeHelper.deserialize(v);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
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
