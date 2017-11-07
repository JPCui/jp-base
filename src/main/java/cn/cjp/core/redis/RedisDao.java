package cn.cjp.core.redis;

import java.io.Serializable;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisDao implements IRedisDao {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private RedisTemplate<Serializable, Serializable> redisTemplate;

    private long defaultExpireTime = 600;

    @Override
    public void setDefaultExpireTime(long expireTime) {
        this.defaultExpireTime = expireTime;
    }

    @Override
    public long getDefaultExpireTime() {
        return this.defaultExpireTime;
    }

    public void save(String key, Serializable value) {
        save(key, value, this.defaultExpireTime);
    }

    public void save(final String key, final Serializable value, final long expireTime) {
        try {
            String newKey = key;
            redisTemplate.execute(new RedisCallback<Serializable>() {
                public Serializable doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] k = redisTemplate.getStringSerializer().serialize(newKey);
                    @SuppressWarnings("unchecked")
                    byte[] v = ((RedisSerializer<Serializable>) redisTemplate.getValueSerializer()).serialize(value);
                    conn.set(k, v);
                    conn.expire(k, expireTime);
                    return null;
                }
            });
        } catch (Exception ex) {
            logger.error("Redis保存数据失败:{}", ex.getMessage());
        }
    }

    public long incr(final String key) {
        return this.incr(key, this.defaultExpireTime);
    }

    public long incr(String key, long expireTime) {
        return this.incr(key, expireTime, false);
    }

    public long incrWithoutExpireTime(final String key) {
        return this.incr(key, this.defaultExpireTime, true);
    }

    public long incr(final String key, final long expireTime, final boolean withoutExpireTime) {
        try {
            String newKey = key;
            return (long) redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] k = redisTemplate.getStringSerializer().serialize(newKey);
                    long ret = conn.incr(k).longValue();
                    if (!withoutExpireTime) {
                        conn.expire(k, expireTime);
                    }
                    return ret;
                }
            });
        } catch (Exception ex) {
            logger.error("Redis自增出错:{}", ex.getMessage());
            return 0;
        }
    }

    public void setExpireTime(final String key, final long expireTime) {
        try {
            String newKey = key;
            redisTemplate.execute(new RedisCallback<Serializable>() {
                public Serializable doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] k = redisTemplate.getStringSerializer().serialize(newKey);
                    conn.expire(k, expireTime);
                    return null;
                }
            });
        } catch (Exception ex) {
            logger.error("Redis设置过期时间失败:{}", ex.getMessage());
        }
    }

    public Object read(final String key) {
        try {
            String newKey = key;
            return redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] k = redisTemplate.getStringSerializer().serialize(newKey);
                    if (conn.exists(k)) {
                        byte[] v = conn.get(k);
                        return redisTemplate.getValueSerializer().deserialize(v);
                    }
                    return null;
                }
            });
        } catch (Exception ex) {
            logger.error("Redis读取数据失败:{}, trace: {}", ex.getMessage(), ex.getStackTrace());
            return null;
        }
    }

    public Long readIncr(final String key) {
        try {
            String newKey = key;
            Object ret = redisTemplate.execute(new RedisCallback<Object>() {
                public Long doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] k = redisTemplate.getStringSerializer().serialize(newKey);
                    if (conn.exists(k)) {
                        byte[] v = conn.get(k);
                        long longValue = Long.parseLong(new String(v));
                        return longValue;
                    }
                    return null;
                }
            });
            return (Long) ret;
        } catch (Exception ex) {
            logger.error("Redis读取数据失败:{}", ex.getMessage());
            return null;
        }
    }

    public void delete(final String key) {
        try {
            String newKey = key;
            redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection conn) throws DataAccessException {
                    return conn.del(redisTemplate.getStringSerializer().serialize(newKey));
                }
            });
        } catch (Exception ex) {
            logger.error("Redis删除数据失败:{}", ex.getMessage());
        }
    }

    public void delete(final byte[] key) {
        try {
            redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection conn) throws DataAccessException {
                    return conn.del(key);
                }
            });
        } catch (Exception ex) {
            logger.error("Redis删除数据失败:{}", ex.getMessage());
        }
    }

    public byte[] get(final byte[] key) {
        try {
            return redisTemplate.execute((RedisConnection conn) -> {
                if (conn.exists(key)) {
                    byte[] v = conn.get(key);
                    return v;
                }
                return null;
            });
        } catch (Exception ex) {
            logger.error("Redis读取数据失败:", ex);
            return null;
        }
    }

    public String get(String key) {
        try {
            return redisTemplate.execute((RedisConnection conn) -> {
                byte[] k = redisTemplate.getStringSerializer().serialize(key);
                if (conn.exists(k)) {
                    byte[] v = conn.get(k);
                    String value = redisTemplate.getStringSerializer().deserialize(v);
                    return value;
                }
                return null;
            });
        } catch (Exception ex) {
            logger.error("Redis读取数据失败:", ex);
            return null;
        }
    }

    public byte[] set(final byte[] key, final byte[] value) {
        return set(key, value, defaultExpireTime);
    }

    public byte[] set(final byte[] key, final byte[] value, final long expireTime) {
        try {
            redisTemplate.execute((RedisConnection conn) -> {
                conn.set(key, value);
                conn.expire(key, expireTime);
                return value;
            });
        } catch (Exception ex) {
            logger.error("Redis保存数据失败:", ex);
        }
        return value;
    }

    public Set<byte[]> keys(final String pattern) {
        try {
            return redisTemplate.execute((RedisConnection conn) -> {
                byte[] k = redisTemplate.getStringSerializer().serialize(pattern);
                if (conn.exists(k)) {
                    Set<byte[]> v = conn.keys(k);
                    return v;
                }
                return null;
            });
        } catch (Exception ex) {
            logger.error("Redis保存数据失败:", ex);
        }

        return null;
    }

    @Override
    public int setnx(String key, String value) {
        return redisTemplate.execute((RedisConnection conn) -> {
            byte[] k = redisTemplate.getStringSerializer().serialize(key);
            byte[] v = redisTemplate.getStringSerializer().serialize(value);
            return conn.setNX(k, v) ? 1 : 0;
        });
    }

    @Override
    public int expire(String key, int timeout) {
        return redisTemplate.execute((RedisConnection conn) -> {
            byte[] k = redisTemplate.getStringSerializer().serialize(key);
            return conn.expire(k, timeout) ? 1 : 0;
        });
    }

    @Override
    public int ttl(String key) {
        return redisTemplate.execute((RedisConnection conn) -> {
            byte[] k = redisTemplate.getStringSerializer().serialize(key);
            return conn.ttl(k).intValue();
        });
    }

    @Override
    public int del(String key) {
        return redisTemplate.execute((RedisConnection conn) -> {
            byte[] k = redisTemplate.getStringSerializer().serialize(key);
            return conn.del(k).intValue();
        });
    }

    @Override
    public String getset(String key, String value) {
        return redisTemplate.execute((RedisConnection conn) -> {
            byte[] k = redisTemplate.getStringSerializer().serialize(key);
            byte[] v = redisTemplate.getStringSerializer().serialize(value);
            byte[] r = conn.getSet(k, v);
            String result = redisTemplate.getStringSerializer().deserialize(r);
            return result;
        });
    }
}
