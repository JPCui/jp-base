package cn.cjp.core.cache;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface Cache {

    public void set(String key, Object value, long expireTime);

    public <T> T get(Class<T> c, String key);

    public Long delete(String key);

    public Long delete(Collection<String> key);

    public Long sadd(String key, Object... values);

    public <T> List<T> smembers(Class<T> c, String key);

    public <T> T execute(CacheCallback<T> callback);

    /**
     * 根据key构建一个带domain的key
     */
    public String getDomainKey(String key);

    public Set<String> keys(String k);

}
