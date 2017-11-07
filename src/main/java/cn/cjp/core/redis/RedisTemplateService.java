package cn.cjp.core.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import cn.cjp.core.cache.Cache;
import cn.cjp.core.cache.CacheCallback;
import cn.cjp.utils.SerializeHelper;

public class RedisTemplateService implements Cache {

    @Value("${redis.expires:1800}")
    private long defaultExpireTime;

    @Value("${redis.key.domain.prefix:domain_key:}")
    private String defaultDomainKeyPrefix;

    RedisTemplate<String, Object> redisTemplate;

    /**
     * 为项目增加redis key的前缀防止key混淆
     *
     * @param key
     */
    public String getDomainKey(String key) {
        return defaultDomainKeyPrefix.concat(key);
    }

    public void set(final String key, final Object value, long expireTime) {
        if (expireTime <= 0) {
            expireTime = defaultExpireTime;
        }
        final long expire = expireTime;
        redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection conn) throws DataAccessException {
                byte[] k = getDomainKey(key).getBytes();
                byte[] v = SerializeHelper.serialize(value);
                conn.set(k, v);
                conn.expire(k, expire);
                return null;
            }
        });
    }

    public Long delete(String key) {
        this.redisTemplate.delete(key);
        return 1L;
    }

    public Long delete(Collection<String> key) {
        this.redisTemplate.delete(key);
        return 1L;
    }

    public void deleteByPrefix(final String keyPrefix) {
        this.redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection conn) throws DataAccessException {
                byte[] k = getDomainKey(keyPrefix).getBytes();
                Set<byte[]> keys = conn.keys(k);
                if (!keys.isEmpty()) {
                    conn.del(keys.toArray(new byte[0][]));
                }
                return null;
            }
        });
    }

    public Long sadd(String key, Object... values) {
        return this.redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection conn) throws DataAccessException {
                byte[] k = getDomainKey(key).getBytes();
                byte[][] vs = new byte[values.length][];
                for (int i = 0; i < values.length; i++) {
                    vs[i] = SerializeHelper.serialize(values[i]);
                }
                return conn.sAdd(k, vs);
            }
        });
    }

    public <T> T execute(CacheCallback<T> c) {
        return c.doIntern(this);
    }

    @Override
    public <T> T get(Class<T> c, String key) {
        return this.redisTemplate.execute(new RedisCallback<T>() {
            public T doInRedis(RedisConnection conn) throws DataAccessException {
                byte[] k = getDomainKey(key).getBytes();
                byte[] v = conn.get(k);
                @SuppressWarnings("unchecked")
                T value = (T) SerializeHelper.deserialize(v);
                return value;
            }
        });
    }

    @Override
    public <T> List<T> smembers(Class<T> c, String key) {
        return this.redisTemplate.execute(new RedisCallback<List<T>>() {
            @SuppressWarnings("unchecked")
            public List<T> doInRedis(RedisConnection conn) throws DataAccessException {
                byte[] k = getDomainKey(key).getBytes();
                Set<byte[]> vs = conn.sMembers(k);
                List<T> values = new ArrayList<>();
                vs.forEach(v -> {
                    values.add((T) SerializeHelper.deserialize(v));
                });
                return values;
            }
        });
    }

    @Override
    public Set<String> keys(String k) {
        return this.redisTemplate.keys(k);
    }

}
