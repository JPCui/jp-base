package cn.cjp.core.cache.redis;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cn.cjp.utils.SerializeHelper;

public class RedisService {

    @Value("${redis.expires:1800}")
    private long defaultExpireTime;

    @Value("${redis.key.domain.prefix:domain_key:}")
    private String defaultDomainKeyPrefix;

    @Autowired
    private IRedisDao redis;

    @PostConstruct
    private void init() {
        this.redis.setDefaultExpireTime(defaultExpireTime);
    }

    /**
     * 为项目增加redis key的前缀防止key混淆
     *
     * @param key
     */
    private String getDomainKey(String key) {
        return defaultDomainKeyPrefix.concat(key);
    }

    public Object getWithoutKeyPrefix(String key) {
        return this.redis.read(key);
    }

    public void setWithoutKeyPrefix(String key, Serializable value) {
        this.redis.save(key, value);
    }

    public void setWithoutKeyPrefix(String key, Serializable value, long expireTime) {
        this.redis.save(key, value, expireTime);
    }

    public void setExpireTimeWithoutKeyPrefix(final String key, final long expireTime) {
        redis.setExpireTime(key, expireTime);
    }

    public void deleteWithoutKeyPrefix(String key) {
        redis.delete(getDomainKey(key));
    }

    public Object get(String key) {
        return redis.read(this.getDomainKey(key));
    }

    public byte[] get(final byte[] key) {
        return redis.get(key);
    }

    public Long getIncr(String key) {
        return redis.readIncr(this.getDomainKey(key));
    }

    public void set(String key, Serializable value) {
        redis.save(this.getDomainKey(key), value);
    }

    public void set(String key, Serializable value, long expireTime) {
        redis.save(this.getDomainKey(key), value, expireTime);
    }

    public void set(final byte[] key, final byte[] value, long expireTime) {
        redis.set(key, value, expireTime);
    }

    public long incr(final String key) {
        return redis.incr(this.getDomainKey(key));
    }

    public long incr(final String key, final long expireTime) {
        return redis.incr(this.getDomainKey(key), expireTime);
    }

    public long incrWithoutExpireTime(final String key) {
        return redis.incrWithoutExpireTime(this.getDomainKey(key));
    }

    public void setExpireTime(final String key, final long expireTime) {
        redis.setExpireTime(this.getDomainKey(key), expireTime);
    }

    public void delete(String key) {
        redis.delete(this.getDomainKey(key));
    }

    public void delete(final byte[] key) {
        redis.delete(key);
    }

    public <T> T getSet(String key, Callable<T> initFunc) {
        T result = null;
        byte[] keys = this.getDomainKey(key).getBytes();
        try {
            byte[] value = this.redis.get(keys);

            if (value == null) {
                result = initFunc.call();
                value = SerializeHelper.serialize(result);
                this.redis.set(keys, value);

            } else {
                result = (T) SerializeHelper.deserialize(value);
            }
            return result;

        } catch (Exception e) {
            try {
                result = initFunc.call();// redis出错时依然返回数据
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    public Set<byte[]> keys(final String pattern) {
        return redis.keys(pattern);
    }
}
