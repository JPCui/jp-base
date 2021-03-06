package cn.cjp.core.redis;

import java.io.Serializable;
import java.util.Set;

public interface IRedisDao {
    /**
     * 设置默认过期时间
     * 
     * @param expireTime
     */
    public void setDefaultExpireTime(long expireTime);

    /**
     * 读取默认过期时间
     * 
     * @return
     */
    public long getDefaultExpireTime();

    /**
     * 使用默认过期时间
     * 
     * @param key
     * @param value
     */
    public void save(String key, Serializable value);

    /**
     * 使用给定过期时间
     * 
     * @param key
     * @param value
     * @param expireTime
     *            单位:秒
     */
    public void save(String key, Serializable value, long expireTime);

    /**
     * @param key
     * @return
     */
    public Object read(String key);

    /**
     *
     * @param key
     * @return
     */
    public Long readIncr(String key);

    /**
     * @param key
     */
    public void delete(String key);

    public void delete(final byte[] key);

    /**
     *
     * @param key
     */
    public long incr(final String key);

    /**
     *
     * @param key
     */
    public long incrWithoutExpireTime(final String key);

    /**
     *
     * @param key
     * @param expireTime
     */
    public long incr(final String key, final long expireTime);

    /**
     *
     * @param key
     * @param expireTime
     */
    public void setExpireTime(final String key, final long expireTime);

    byte[] get(final byte[] key);

    String get(final String key);

    byte[] set(final byte[] key, final byte[] value);

    byte[] set(final byte[] key, final byte[] value, final long expireTime);

    Set<byte[]> keys(final String pattern);

    public int setnx(String key, String string);

    public int expire(String key, int timeout);

    public int ttl(String key);

    public int del(String key);

    public String getset(String key, String deadlineStr);
}
